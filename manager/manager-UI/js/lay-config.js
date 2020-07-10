/**
 * date:2019/08/16
 * author:Mr.Chung
 * description:此处放layui自定义扩展
 * version:2.0.4
 */

/**
* 后台模块配置
*/
window.module = {
    system: {
        // 模块地址
        server: 'http://192.168.1.237:10000/manager_sys/',
        // 模块文件夹名称
        dir: 'sys'
    },
    jj: {
        // 模块地址
        server: 'http://192.168.1.237:8099/manager_jj/',
        dir: 'jj'
    },
    fc: {
        // 模块地址
        server: 'http://192.168.1.237:8099/manager_jj/',
        dir: 'fc'
    },

    sf: {
        // 模块地址
        server: 'http://localhost:10000/manager_sys/',
        // 模块文件夹名称
        dir: 'sf'
    }
}

window.rootPath = (function (src) {
    src = document.scripts[document.scripts.length - 1].src;
    return src.substring(0, src.lastIndexOf("/") + 1);
})();

layui.config({
    base: rootPath + "lay-module/",
    version: true
}).extend({
    miniAdmin: "layuimini/miniAdmin", // layuimini后台扩展
    miniMenu: "layuimini/miniMenu", // layuimini菜单扩展
    miniTab: "layuimini/miniTab", // layuimini tab扩展
    miniTheme: "layuimini/miniTheme", // layuimini 主题扩展
    step: 'step-lay/step', // 分步表单扩展
    treeTable: 'treetable-lay/treeTable', //table树形扩展
    tableSelect: 'tableSelect/tableSelect', // table选择扩展
    iconPickerFa: 'iconPicker/iconPickerFa', // fa图标选择扩展
    echarts: 'echarts/echarts', // echarts图表扩展
    echartsTheme: 'echarts/echartsTheme', // echarts图表主题扩展
    wangEditor: 'wangEditor/wangEditor', // wangEditor富文本扩展
    layarea: 'layarea/layarea', //  省市县区三级联动下拉选择器
});
