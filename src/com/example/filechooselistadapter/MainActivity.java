package com.example.filechooselistadapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.filechooselistadapter.MyListAdapter.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	ArrayList<HashMap<String,Object>> list=null;
	HashMap<String,Object> map=null;
	String initPath=null;
	MyListAdapter mla=null;
	
	ListView listView=null;
	TextView text_file_path=null;
	Button btn_back=null;
	Button btn_confirm=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//获取顶端显示路径的TextView
		text_file_path=(TextView)findViewById(R.id.text);
		//设置最初的路径为根目录
		initPath=Environment.getExternalStorageDirectory().getAbsolutePath();
		File f=new File(initPath);
		FileInfo initFi=new FileInfo(f.getAbsolutePath(), f.getName(), f.isDirectory());
		//为list分配空间
		list=new ArrayList<HashMap<String,Object>>();

		//通过findViewById找到住布局文件中的listView
		listView = (ListView) findViewById(R.id.list);
		//new自定义的Adapter
		mla=new MyListAdapter(this,list,initFi);
		//初始更新视图
		mla.updateList(initFi);
		
		//设置当ListView被点击后，更新当前目录
		listView.setOnItemClickListener(myItemClickListener);
		
		//绑定Adapter
		listView.setAdapter(mla);
		//显示路径
		text_file_path.setText(mla.getCurrentFi().getFilePath());

		btn_back=(Button)findViewById(R.id.btn_back);
		btn_confirm=(Button)findViewById(R.id.btn_confirm);
		//点击返回返回上层目录
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File f=new File(mla.getCurrentFi().getFilePath());
				//如果不是到根目录，则返回父文件夹
				if(!f.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())){
					File f_parent=f.getParentFile();
					FileInfo fi_parent=new FileInfo(f_parent.getAbsolutePath(),f_parent.getName(),f_parent.isDirectory());
					mla.updateList(fi_parent);
					text_file_path.setText(fi_parent.getFilePath());
				}
				//否则，弹出确认对话框是否退出
				else{
					new AlertDialog.Builder(MainActivity.this).setTitle("确认退出吗？") 
		            .setIcon(android.R.drawable.ic_dialog_info) 
		            .setPositiveButton("确定", new DialogInterface.OnClickListener() { 
		         
		                @Override 
		                public void onClick(DialogInterface dialog, int which) { 
		                // 点击“确认”后的操作 
		                    MainActivity.this.finish(); 
		                } 
		            }) 
		            .setNegativeButton("返回", new DialogInterface.OnClickListener() { 
		         
		                @Override 
		                public void onClick(DialogInterface dialog, int which) { 
		                // 点击“返回”后的操作,这里不设置没有任何操作 
		                } 
		            }).show(); 
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 *ListView 的item被点击后执行
	 *如果是文件夹，则扫描，更新数据，更新视图update
	 *如果是文件，加入选择的set中
	 */
	private AdapterView.OnItemClickListener myItemClickListener =new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			//获取点击的位置的文件信息
			FileInfo fi=(FileInfo)((HashMap<String, Object>)mla.getItem(position))
					.get(MyListAdapter.mapKey[2]);
			//设置路径
			text_file_path.setText(fi.getFilePath());
			//Toast.makeText(MainActivity.this,position+"listview is clicked" , Toast.LENGTH_SHORT).show();
			//如果是文件夹，更新ListView
			if(fi.isDirectory())
				mla.updateList(fi);
			//如果是文件
			else{
				mla.updateItemSelect(position);
			}
		}
	};
}
