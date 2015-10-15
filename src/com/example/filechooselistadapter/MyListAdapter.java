package com.example.filechooselistadapter;

/*
 * 更新自定义ListView的顺序
 * 1.更新String []mapKey
 * 2.更新Class ViewHolder
 * 3.更新function getView()
 * 4.更新function updateList()
 * 5.更新Class MainActivity->function setData()
 * 
 * 封装很重要啊！
 * */

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MyListAdapter extends BaseAdapter{

	private Context context=null;
    private ArrayList<HashMap<String,Object>> list=null;
    private LayoutInflater layoutInflater=null;
    /**
     * list保存map,map存放的Object的键值
     */
	public static final String [] mapKey={"image","text_file_info","file_info","image_select"};
    private FileInfo currentFi=null;
    /**
     * 点击选择的文件的集合
     */
    private HashSet<FileInfo> selectFileSet=null;
    /**
    * 这里仿照的是SimpleAdapter的形参列表
    * @param context
    * @param list
    */

   public MyListAdapter(Context context,
           ArrayList<HashMap<String, Object>> list,FileInfo currentFi) {
       super();
       this.context = context;
       this.list = list;
       this.layoutInflater=LayoutInflater.from(context);
       this.currentFi=currentFi;
       this.selectFileSet=new HashSet<FileInfo>();
   }

   	/**
   	 * list 中数据的数量
   	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 获取list中相应position的map
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	
	/**
     * 为了为convertView设置附加信息Tag，这里创建一个内部类ViewHolder，用于盛放一行中所有控件的引用，将这些引用
     * 实例化后作为convertView的附加信息。
     */
	class ViewHolder{
		public ImageView imgView=null;
		public TextView tx=null;
		public ImageView imgView_select=null;
		/*注意View和Activity都属于容器类，都需要设置布局文件，内部都含有子控件，且都有findViewById()
        * 他们之间没有明显的继承关系
        */
		public ViewHolder(){};
		public ViewHolder(View convertView){
			imgView=(ImageView)convertView.findViewById(R.id.imgView);
			tx=(TextView)convertView.findViewById(R.id.text_file_info);
			imgView_select=(ImageView)convertView.findViewById(R.id.imgView_select);
		}
	}
	
	/**
	 * 
	 * @author xcw
	 *每个item被点击过后执行的侦听函数的类参数，建议还是在Activity中绑定到ListView上
	 */
	class ImageListener implements OnClickListener{
		private int position=0;
		public ImageListener(int p){
			this.position=p;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
		}
		
	}
	
	/**
     * getView方法为系统在绘制每一行时调用，在此方法中要设置需要显示的文字，图片，
     * 以及为按钮设置监听器。
     * 
     * 形参意义：
     * position：当前绘制的item 的位置（ID）；
     * convertView，系统在绘制ListView时，如果是绘制第一个Item（即第一行），convertView为null,当
     * 绘制第二个及以后的Item的convertView不为空，这时可以直接利用这个convertView的getTag()方法，获得各控件
     * 的实例，并进行相应的设置，这样可以加快绘图速度。
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder vh=new ViewHolder();
		if(convertView==null){
			convertView=layoutInflater.inflate(R.layout.list_view_layout_detail, null); 
			vh=new ViewHolder(convertView);
			convertView.setTag(vh);
		}
		else{
			vh=(ViewHolder)convertView.getTag();
		}
		//绑定数据
		vh.imgView.setBackgroundResource((Integer)list.get(position).get(mapKey[0]));
		vh.tx.setText((String)list.get(position).get(mapKey[1]));
		vh.imgView_select.setBackgroundResource((Integer)list.get(position).get(mapKey[3]));
		//convertView.setOnClickListener(new ImageListener(position));
		
		 /**
         * 首先判断是不是第一次创建Item，若是，则创建convertView实例和ViewHolder对象，并通过fandViewById()方法
         * 获得每一行中所有空间的实例放在ViewHolder对象中，然后对convertView设置标签
         */

		return convertView;
	}

	//将FileInfo下所有的文件及文件夹，更新至list视图
	public void updateList(FileInfo fi){
		currentFi=fi;
		if(list!=null)
			list.clear();
		File currentFile=new File(fi.getFilePath());
		File []childFiles=currentFile.listFiles();
		HashMap<String, Object> map=null;
		for(int i=0;(childFiles!=null&&i<childFiles.length);i++){
			map=new HashMap<String, Object>();
			FileInfo myFI=new FileInfo(childFiles[i].getAbsolutePath(),
					childFiles[i].getName(),childFiles[i].isDirectory());
			if(myFI.isDirectory()){
				map.put(mapKey[0], R.drawable.ic_folder);
			}
			else
				map.put(mapKey[0], R.drawable.ic_file);
			
			map.put(mapKey[1], myFI.getFileName());
			map.put(mapKey[2], myFI);
			if(!myFI.isDirectory())
				map.put(mapKey[3], R.drawable.ic_circle_unselect);
			else
				map.put(mapKey[3], R.drawable.transparent);
			list.add(map);
		}
		/**
		 * 调用此函数，当adapter的数据改变时，通知与其绑定的ListView更新视图
		 */
		notifyDataSetChanged();
	}
	public void updateItemSelect(int position){
		FileInfo fi=(FileInfo)list.get(position).get(mapKey[2]);
		if(!fi.isDirectory()){
			HashMap<String, Object> map=list.get(position);
			if(!selectFileSet.contains((FileInfo)map.get(mapKey[2]))){
				selectFileSet.add((FileInfo)map.get(mapKey[2]));
				map.put(mapKey[3], R.drawable.ic_circle_ok);
			}
			else{
				selectFileSet.remove((FileInfo)map.get(mapKey[2]));
				map.put(mapKey[3], R.drawable.ic_circle_unselect);
			}
			notifyDataSetChanged();
		}
	}
	
	public FileInfo getCurrentFi(){
		return this.currentFi;
	}
}
