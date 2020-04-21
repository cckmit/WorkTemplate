package com.tools;

import com.code.json.PermissionJson;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tools.db.OpDbConnector;
import com.tools.db.OpDbGamePackage;
import com.tools.db.OpDbPersieValue;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

@WebListener
public class Listener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {

        OpDbConnector.init();
        OpDbGamePackage.init();

        OpDbPersieValue.init();
        //初始化权限json文件
        initPerssionJson(arg0);
    }


    private void initPerssionJson(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        String relativelyPath = servletContext.getRealPath("/");
        System.out.println(relativelyPath);
        File jsonFile = new File(relativelyPath, "/pages/tree_data.json");
        System.out.println(jsonFile.getName());
        List<PermissionJson> permissionJsonList = null;
        Type type = new TypeToken<List<PermissionJson>>() {
        }.getType();
        try {
            String jsonStr = XwhTool.readInputStream(new FileInputStream(jsonFile));
            permissionJsonList = new Gson().fromJson(jsonStr, type);
            servletContext.setAttribute("permissionJsonList", permissionJsonList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
