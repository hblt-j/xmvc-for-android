package xt.crm.mobi.o.test;

import xt.crm.mobi.c.base.BaseActivity;
import xt.crm.mobi.o.util.Debuger;
import xt.mobi.jar.sources.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class daoTestActivity extends BaseActivity {
	//public static Dao DAO;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		final TextView t = (TextView) findViewById(R.id.textView1);

		//===========test log=============
		//Debuger.d("test", "content");
		//定期搜索log下文件上传。。
		//Debuger.delFile();//删除超过保留期log
		
		Debuger.cmd="logcat -d -b main -v time *:e";
		Debuger.SysLog();
		//==================test 未捕获异常触发 crashhandler================
		//主线程运行时err
	        String i=null ;  
	        i.equals("");  
		new Thread() {  //线程运行时err
             public void run() {  
                 Log.d("ANDROID_LAB", "Thread.child.run()");  
                 int i = 0;  
                 i = 100 / i;  
             }  
         }.start();  
		//============test alarm================
//		ActiDAO actdao = new ActiDAO(getApplicationContext());
//		Action act;
//		try {
//			act = (Action) actdao.DAO.queryForId(7);
//			act.st2=new Date().getTime()/1000+3+"";
//			actdao.modi(act);
//			Log.d("time", new Date().getTime()+"");
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		
		
//		try {//===================test af service====================
//			CustDAO userdao = new CustDAO(daoTestActivity.this);
//			//模拟action入参：
//			Customer user= (Customer) userdao.dao.queryForId(1);//模拟界面vo
//			String file =FileUtil.sdPath+ctrler.getSystemProperty("photopath")+"xt.JPG";//模拟拍照临时路径
//			userdao.addAttachment(user, file, "图片");
//			Log.d("af", userdao.vo.af);
////			userdao.delAttachment(user, "027548982d719df15eba7e68d39dbc03");
//			//执行返回至handler
//			if(userdao.errs.size()>0){
//				Toast.makeText(daoTestActivity.this, userdao.errs.get(0), Toast.LENGTH_LONG);
//				//return false;
//			}
//			if(userdao.wars.size()>0){
//				Toast.makeText(daoTestActivity.this, userdao.wars.get(0), Toast.LENGTH_LONG);
//			}
//			//结果：
//			//{"eb7202eb360c5c7a076690e28efaed21":{"isdel":0,"isup":0,"type":"图片","F":"eb7202eb360c5c7a076690e28efaed21.jpg"},"0c97a694516430e9f393eabaeecc0a02":{"type":"图片","isup":1,"isdel":0,"F":"0c97a694516430e9f393eabaeecc0a02.jpg"}}
//			
//			List<String> ls=userdao.getPicList();//界面获取path list
//			for (String path : ls) {
//				Log.d("path", path);
//				Toast.makeText(daoTestActivity.this, path, Toast.LENGTH_LONG);
//			}
//			//结果：
//			///mnt/sdcard/xtools/order/1/pic/e/s-eb7202eb360c5c7a076690e28efaed21.jpg
//			///mnt/sdcard/xtools/order/1/pic/0/s-0c97a694516430e9f393eabaeecc0a02.jpg
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		//＝＝＝＝＝＝＝＝＝＝＝＝test 关联删除＝＝＝＝＝＝＝＝＝＝＝＝＝
//		t.setText("加载自动为id=1号客户加附件一张，点我删除1号客户");
//		t.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				Customer cu = new Customer();
//				cu.id=1;
//				CustDAO userdao = new CustDAO(daoTestActivity.this);
//				userdao.del(cu);
//				if(userdao.errs.size()>0){
//					Toast.makeText(daoTestActivity.this, userdao.errs.get(0), Toast.LENGTH_LONG);
//					//return false;
//				}else{
//					Toast.makeText(daoTestActivity.this, "删除一号客户成功", Toast.LENGTH_LONG);
//				}	
//			}
//		});
		
		
		/********************************************************************************************************************************************

			应用表（目前的应用表主要在手机端支持与服务器的同步以及全文检索）
		应用表的特点：


		调用数据库规范：
		遵循： 
		1、检索
		2、添加
			$db=new db_customer()
			.........
			//开始一次添加数据
			$db->db_init();	//初始化数据
			$db->df=	需要添加的数据（数组或者存储结构或者存储customer的类）
			$db->comp_in();	//处理内部数据
			if ($db->db_chk())
			{	//$db->insert();
				//$db->comp_out();
				//$db->save_data();
			}
			else
			{	//显示错误
			}
		3、更新
			$db=new db_customer()
			.........
			//开始一次更新数据
			$db->db_init();	//初始化数据
			$db->load_data($id);	//准备更新id号数据
			$db->df=	需要变更的数据（数组或者存储结构或者存储customer的类）
			$db->comp_in();	//处理内部数据
			if ($db->db_chk())
			{	$db->save_data();
			}
			else
			{	//显示错误
			}
			
		4、删除
			$db=new db_customer()
			.........
			//开始删除数据
			$db->db_init();	//初始化数据
			$db->load_data($id);	//准备更新id号数据
			if ($db->db_chkdel())
			{	$db->delete();
			}


		5、添加一个附件
			$db=new dt_xx()
			.........
			//开始添加附件
			$db->db_init();	//初始化数据
			$db->load_data($id);	//准备更新id号数据
			if($db->addAttr($file))	//添加一个附件
			{	//成功
			}
			else
			{	//失败，失败信息在err 中
			}

		6、删除一个附件
			$db=new dt_xx()
			.........
			//开始添加附件
			$db->db_init();	//初始化数据
			$db->load_data($id);	//准备更新id号数据
			if($db->delAttr($md5))	//添加一个附件
			{	//成功
			}
			else
			{	//失败，失败信息在err 中
			}

			


		********************************************************************************************************************************************/

		
		//-----------test custdao begin -----------------
		/*Customer customer = new Customer();
		customer.name="刘海";
//		customer.name="张海波";
//		customer.name="王天虹";
		customer.com="天津飞奥德科技有限公司";
//		customer.com="北京飞奥德科技有限公司";
//		customer.com="上海飞奥德科技有限公司";
		customer.tel="18601182121";
//		customer.tel="18601182120";
//		customer.tel="18601182110";
		customer.email="liuhai@gmail.com";
//		customer.email="zhanghaibo@gmail.com";
//		customer.email="wangtianhong@gmail.com";
		customer.headship="副总";
//		customer.headship="总经理";
//		customer.headship="经理";
		//customer.df =0;
			
		CustDAO CustDao = new CustDAO(daoTestActivity.this);
		Log.i("before add",customer.toString());
		
		CustDao.add(customer);
		Toast.makeText(daoTestActivity.this, CustDao.wars.size()>0?CustDao.wars.get(0):"", Toast.LENGTH_LONG).show();
		Log.i("after add", customer.toString());
		//CustDao.DAO.query(arg0)
		customer.id=CustDao.vo.id;
		customer.name="王天虹";
		Log.i("before modi", customer.toString());  
		CustDao.modi(customer);
		Toast.makeText(daoTestActivity.this, CustDao.wars.size()>0?CustDao.wars.get(0):"", Toast.LENGTH_LONG).show();
		Log.i("after modi", customer.toString());
		
		Log.i("before del", customer.toString());
		CustDao.del(customer);
		Toast.makeText(daoTestActivity.this, CustDao.wars.size()>0?CustDao.wars.get(0):"", Toast.LENGTH_LONG).show();
		//Log.i("after del", customer.toString());
		*/
		//---test custdao end ---

	
	//＝＝＝＝＝＝＝＝＝＝＝sqlite CURD＝＝＝＝＝＝＝＝＝＝＝＝＝＝
	// 2.dao实例化:
	// Dao<Class, Integer> DAO = BaseDAO.Dao;
	// 3.dao操作
	// Object user=DAO.queryForAll();DAO.queryForId(2);

	// QueryBuilder qb = dao.queryBuilder();
	// qb.where().between("TransactionTime", start, end).AND()...;
	// return qb.query();

	// dao.queryRaw(query, arguments)
	// DAO.create(cti); DAO.update(cti);DAO.deleteById(2);
	/*try {
		CTI cti = new CTI();
		DAO=BaseDAO.instanseDao(daoTestActivity.this, CTI.class,"test.db");
		//add
		create(); 
		//删
		//delete(cti);
		//改
		//update(cti);
		//查
		find();
			
	} catch (SQLException e) {
		e.printStackTrace();
	}
	*/
	
	/*
	public void find() throws SQLException {
		//CTI cti = (CTI) DAO.queryForId(2);
		List<CTI> list = DAO.queryForAll();
		for (CTI cti : list) {
			Log.i("CTI", "cti_id="+cti.cti_id+"  cu_id="+cti.cu_id+"  con_id="+cti.con_id+"  anum="+cti.anum+"  bnum="+cti.bnum);
		}
	}

	public void update(CTI cti) throws SQLException {
		cti.cti_id=1;
		cti.cu_id = 100;
		cti.con_id = 100;
		DAO.update(cti);
		//DAO.updateId(cti, 3);
	}

	public void delete(CTI cti) throws SQLException {
		DAO.deleteById(2);
	}

	public void create() throws SQLException {
		for (int i = 0; i < 2; i++) {   
			CTI cti = new CTI();
			cti.cu_id=i;
			cti.con_id=i;
			cti.anum=""+i;
			cti.bnum=""+i;
			//增
			DAO.create(cti);  
			}
	}
	*/
	}
}