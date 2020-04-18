package com.code.ui.servlet;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.annotation.WebServlet;

import com.code.dao.MiniGamebackDao;
import com.code.entity.Notice_state;
import com.code.ui.UIMoudleServlet;
import com.google.gson.reflect.TypeToken;
@WebServlet(urlPatterns = { "/notice_state", "/pages/notice_state" })
public class NoticeStateServlet extends UIMoudleServlet {
	private static final long serialVersionUID = 1L;
	
	public Class<?> getClassInfo() {
		return MiniGamebackDao.class;
	}
	
	protected Type getTypeof() {
		// TODO Auto-generated method stub
		return new TypeToken<Vector<Notice_state>>() {
		}.getType();
	}

	@SuppressWarnings("unchecked")
	public Vector<Notice_state> findData() {
		Vector<Notice_state> list =new Vector<Notice_state>();
		if(UIMoudleServlet.noticeMap!=null&&
				UIMoudleServlet.noticeMap.size()>0)
		{
			for(Map.Entry<String,String> map:UIMoudleServlet.noticeMap.entrySet())
			{
				Notice_state notice_state=new Notice_state();
				notice_state.notice_url=map.getKey();
				String[] strings=map.getValue().split("&&");
				notice_state.notice_time=strings[0];
				notice_state.notice_name=strings[1];
				list.add(notice_state);
			}
		}
		Collections.sort(list,new Comparator<Notice_state>() {
            @Override
            public int compare(Notice_state o1, Notice_state o2) {
                // TODO Auto-generated method stub
                return o2.notice_time.compareTo(o1.notice_time);
            }
        });
		return list;
	}

}
