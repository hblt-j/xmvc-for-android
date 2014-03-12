package xt.crm.db4oHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.events.CancellableObjectEventArgs;
import com.db4o.events.CommitEventArgs;
import com.db4o.events.Event4;
import com.db4o.events.EventListener4;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.ext.DatabaseFileLockedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oIOException;
import com.db4o.ext.IncompatibleFileFormatException;
import com.db4o.ext.OldFormatException;
import com.db4o.query.Query;

/**
 * db4o支持类，实现自增id集成
 * <br>文件名称:DB4OHelper.java<br>
 * <br>内容摘要:<br>
 * <br>修改日期       修改人员   版本	   修改内容 <br>
 * <br>2013-9-4    j     0.1    0.1 新建<br>
 * @author:   j 
 * @version:  0.1  
 * @Date:     2013-9-4 下午03:00:07
 */
public class DB4OHelper {
	public static EmbeddedObjectContainer db;
	public String DBPath="/sdcard/";
	public String DB_NAME = "test.db";	//数据库名
	/**
	 * 创建打开数据库返回数据库链接
	 * 
	 * @param 
	 * @return ObjectContainer
	 * @throws 
	 * 2013-8-28 下午02:23:10
	 */
	public DB4OHelper(){
		File dir=new File(DBPath);
		if (!dir.exists()) {
          dir.mkdirs();// 创建一个目录
		}
		try {
			if(db==null||db.ext().isClosed())db=Db4oEmbedded.openFile(dbConfig() ,DBPath+DB_NAME);
			registerEventOnContainer(db);
			//return db;
		} catch (Db4oIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseFileLockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncompatibleFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OldFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseReadOnlyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DB4OHelper(String path,String dbname){
		DBPath=path;DB_NAME=dbname;
		File dir=new File(path);
		if (!dir.exists()) {
          dir.mkdirs();// 创建一个目录
		}
		try {
			if(db==null||db.ext().isClosed())db=Db4oEmbedded.openFile(dbConfig() ,DBPath+DB_NAME);
			registerEventOnContainer(db);
			//return db;
		} catch (Db4oIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseFileLockedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncompatibleFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OldFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseReadOnlyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 配置db4o
	 * 
	 * @param 
	 * @return EmbeddedConfiguration
	 * @throws 
	 * 2013-8-28 下午02:23:56
	 */
	public EmbeddedConfiguration dbConfig() {
		EmbeddedConfiguration conf=Db4oEmbedded.newConfiguration();
		conf.common().objectClass(IDHolder.class).objectField("id").indexed(true);
//		Db4o.newConfiguration().blockSize(8);
//		conf.common().add(new TransparentActivationSupport());
//		conf.blockSize(int):这个可以配置Db4o文件的大小，参数范围为1-127,默认的是1,也就是默认的最大文件为2G,官方推荐的配置为8,正好是一个指针的长度，这样有利于加快速度
//		conf.activationDepth(int):这个是设置激活的深度，默认值是5,即当使用链表结构的时候最得首对象后最多只能往下走5层，这是为了不浪费内存
//		conf.lockDatabaseFile(true|false):默认值是true，即默认情况下是锁定数据库的，一个数据库文件只允许被一个应用程序读取，如果需要可以把它设为false，这样就能够允许多程序共享一个数据库了
//		conf.unicode(true|false):默认值是true，即默认是采用unicode编码的。采用unicode的好处是支持多国语言，不好的地方是速度变慢，如果没用中文或者为了速度起见，可以把它关闭
//		conf.weakReference(true|false):默认是true，即默认为持有一份对象的本地缓存，好处是使操作更快，缺点是一占内存，二如果要获得别人已经commit的对象要手动refresh一下
		return conf;
	}
    
/**
 * 采用底层事件触发callBack机制自动实现AutoIncrement id的支持
 * 使用方法：bean实例只需继承IDHolder类，即可自动增加一个自增的id字段
 * @param 
 * @return void
 * @throws 
 * 2013-9-4 下午02:54:44
 */
    public void registerEventOnContainer(final ObjectContainer container) {
        // #example: use events to assign the ids
        final AutoIncrement increment = new AutoIncrement(container);
        EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(container);
        eventRegistry.creating().addListener(new EventListener4<CancellableObjectEventArgs>() {
            public void onEvent(Event4<CancellableObjectEventArgs> event4,
                                CancellableObjectEventArgs objectArgs) {
                if(objectArgs.object() instanceof IDHolder){
                    IDHolder idHolder = (IDHolder) objectArgs.object();
                    idHolder.setId(increment.getNextID(idHolder.getClass()));
                }
            }
        });
        eventRegistry.committing().addListener(new EventListener4<CommitEventArgs>() {
            public void onEvent(Event4<CommitEventArgs> commitEventArgsEvent4,
                                CommitEventArgs commitEventArgs) {
                increment.storeState();
            }
        });
        // #end example
    }
	/**  
     * 关闭数据库连接操作  
     * @param db  
     */  
    public void close() {  
        if (db != null) {  
            db.close();  
        }  
    }  
    /**
     * 提交
     * 
     * @param 
     * @return void
     * @throws 
     * 2013-8-29 下午04:56:16
     */
    public void commit(){
    	 if (db != null) db.commit();  
    }
    public <T> T refresh(T t){
   	 	if (db != null) db.ext().refresh(t,5);  
   	 	return t;
    }
    /**
     * 条件查询
     * 
     * @param 
     * @return Query
     * @throws 
     * 2013-8-28 下午02:24:16
     */
    private Query setAttr(Query query, String[] paramNames, Object[] values) {  
        if (paramNames.length > 0) {  
            for (int i = 0; i < paramNames.length; i++) {  
                query.descend(paramNames[i]).constrain(values[i]);  
            }  
        }  
        return query;  
    }  
      /**
       * 模糊条件查询
       * 
       * @param 
       * @return Query
       * @throws 
       * 2013-8-28 下午02:24:49
       */
    private Query setBlurAttr(Query query, String[] paramNames, Object[] values) {  
        if (paramNames.length > 0) {  
            for (int i = 0; i < paramNames.length; i++) {  
                query.descend(paramNames[i]).constrain(values[i]).like();  
            }  
        }  
        return query;  
    }  
      /**
       * 设置 排序
       * 
       * @param 
       * @return Query
       * @throws 
       * 2013-8-28 下午02:25:19
       */
    private Query setOrder(Query query, Map<String, String> order) {  
        if (order != null) {  
            Set<String> keys = order.keySet();  
            for (String str : keys) {  
                if ("asc".equals(str)) {  
                    query.descend(order.get(str)).orderAscending();  
                } else if ("desc".equals(str)) {  
                    query.descend(order.get(str)).orderDescending();  
                }  
            }  
        }  
        return query;  
    }  
    /**  
     * 更新、保存数据  
     * @param entity  
     */  
    public void save(Object entity) {  
        db.store(entity);  
        //db.commit();  
    }  
    /**
     * 删除
     * 
     * @param 
     * @return void
     * @throws 
     * 2013-8-28 下午02:25:39
     */
    public void del(Object entity) {  
        db.delete(entity);  
        //db.commit();  
    }  
    /**
     * QBE对象查询
     * 
     * @param 
     * @return T
     * @throws 
     * 2013-8-28 下午02:25:49
     */
    public <T> T find(T entity) {  
        ObjectSet<T> result = db.queryByExample(entity);  
        T t = null;  
        while (result.hasNext()) {  
            t = result.next();  
        }  
        return t;  
    }  
    /**
     * findById
     * 
     * @param 
     * @return T
     * @throws 
     * 2013-8-28 下午02:26:32
     */
    public <T> T find(Class<T> entity, int id) {  
        Query query = db.query();  
        query.constrain(entity);  
        query.descend("id").constrain(id);  
//      freeResource(db);  
//      return null;
        ObjectSet<T> result = query.execute();  
        T t = null;  
        while (result.hasNext()) {  
            t = result.next();  
        }  
        return t;  
    }  
    /**
     * 条件查询实例
     * 
     * @param 
     * @return T
     * @throws 
     * 2013-8-28 下午02:27:03
     */
    public <T> List<T> findLs(Class<T> entity, String[] paramNames, Object[] values, Map<String, String> order) {  
        List<T> list = new ArrayList<T>();  
        Query query = db.query();  
        query.constrain(entity);  
        query = setAttr(query, paramNames, values);  
        query = setOrder(query, order); 
        ObjectSet<T> result = query.execute();  
        T t;
        while (result.hasNext()) {  
           t = result.next();  
           list.add(t);  
        }  
        return list;  
    }  
    /**
     * findAll加排序
     * 
     * @param 
     * @return List<T>
     * @throws 
     * 2013-8-28 下午02:27:30
     */
    public <T> List<T> findAll(Class<T> entity, Map<String, String> order) {  
        List<T> list = new ArrayList<T>();  
        Query query = db.query();  
        query.constrain(entity);  
        query = setOrder(query, order);  
        ObjectSet<T> result = query.execute();  
        T t;
        while (result.hasNext()) {  
            t = result.next();  
            list.add(t);  
        }  
        return list;  
    }  
    /**
     * 模糊条件查询实例
     * 
     * @param 
     * @return List<T>
     * @throws 
     * 2013-8-28 下午02:27:55
     */
    public <T> List<T> findByBlur(Class<T> entity, String[] paramNames, Object[] values, Map<String, String> order) {  
        Query query = db.query();  
        query.constrain(entity);  
        query = setBlurAttr(query, paramNames, values);  
        query = setOrder(query, order);
        ObjectSet<T> result = query.execute();  
        List<T> list = new ArrayList<T>();  
        T t;
        while (result.hasNext()) {  
            t = result.next();  
            list.add(t);  
        }  
        return list;  
    }  
}
