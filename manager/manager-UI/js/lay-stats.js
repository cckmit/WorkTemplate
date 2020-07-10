/**
 * date:2020/05/01
 * author:chen cheng
 * description: 数据模块适用，数据模块与CRUD模块的不同在于页面只有查询功能，但是查询复杂度较高，同时为方便运营，会有在表格中跳转详情功能
 * version:1.0.1
 */
let $, layer, form, table;

layui.use(['table', 'form', 'layer'], () => {
    $ = layui.jquery, table = layui.table, form = layui.form, layer = layui.layer;

    // 如果定义了动态下拉框，初始化数据
    const existRenderAllSelect = typeof renderAllSelect !== "undefined" && renderAllSelect !== null;
    existRenderAllSelect && renderAllSelect('windowCache');

    // 监听查询表单搜索操作
    form.on('submit(data-search-btn)', (data) => {
        tableReload(data.field);
        return false;
    });

    // 监听查询表单种的重置操作
    $("#data-search-reset").click(() => {
        $("#data-search-form")[0].reset();
        const existRenderAllSelect = typeof renderAllSelect !== "undefined" && renderAllSelect !== null;
        existRenderAllSelect && renderAllSelect('serverDb');
        setTimeout(() => {
            layer.msg('刷新完成！', { icon: 1, time: 2000 });
        }, 1000);
        return false;
    });

    // 表格属性
    let tableOption = {
        id: 'statsTableId',
        elem: '#statsTableId',
        toolbar: '#statsTableBar',
        defaultToolbar: ['filter', 'exports', {
            title: '提示',
            layEvent: 'LAYTABLE_TIPS',
            icon: 'layui-icon-tips'
        }],
        cols: [],
        data: [],
        totalRow: false,
        page: false,
        limit: Number.MAX_VALUE,
        loading: true,
        event: true,
        size: 'sm',
        height: 'full-120'
    };

    // 查询参数记录，用于返回操作
    let queryDataRecode = [];

    // 点击详情时分组方式
    let detailGroupBy;

    // 查询方式一：进入页面执行默认查询
    $(() => {
        // 获取查询条件数据
        const queryData = form.val("data-search-form");
        // 将数据放入返回队列
        queryDataRecode.push(queryData);
        // 通过Ajax查询数据
        getList('auto', queryData);
        return false;
    });

    // 查询方式二：监听查询表单点击事件，根据参数查询
    form.on('submit(data-search-btn)', (data) => {
        // 获取查询条件数据
        const queryData = data.field;
        // 将数据放入返回队列
        queryDataRecode.push(queryData);
        // 通过Ajax查询数据
        getList('submit', queryData);
        return false;
    });

    // 查询方式三：监听详情按钮，先给表单赋值，再模拟表单点击查询
    table.on('tool(statsTableFilter)', (obj) => {
        if ('detail' === obj.event) {
            // 判断当前数据是否有详情
            if (obj.data.haveDetail) {
                // 查询表单重新赋值
                let formObject = buildQueryFormObject(obj.data);
                console.log('formObject ', formObject);
                // 查询表单渲染
                for (let key in formObject) {
                    $('#' + key).val(formObject[key]);
                }
                $('#groupType').val(detailGroupBy);
                form.render();
                // 取值
                const queryData = form.val("data-search-form");
                // 放入回退数据，用于查询
                queryDataRecode.push(queryData);
                // 查询
                getList('detail', queryData);
            } else {
                layer.msg('当前数据冇得详情！', { icon: 0, time: 2000 });
            }
        }
        return false;
    });

    // 查询方式四：点击表头toolBar返回按钮时查询
    table.on('toolbar(statsTableFilter)', (obj) => {
        if ('return' === obj.event) {
            if (queryDataRecode.length > 1) {
                // 先移除最后一次的请求参数
                queryDataRecode.pop();
                // 再使用最后一个参数，但是不移除，理解这两步操作请自行画图观察
                let queryData = queryDataRecode[queryDataRecode.length - 1];
                // 重置查询表单之后重新赋值
                $('#data-search-form')[0].reset();
                form.val('data-search-form', queryData);
                getList('return', queryData);
            } else {
                layer.msg('当前已经是最初始页面，不能再返回啦！', { icon: 0, time: 2000 });
            }
        }
        return false;
    });

    /**
     * 通过Ajax方式执行查询
     */
    function getList(operator, queryData) {
        console.log('queryData -> ', operator, ' : ', queryDataRecode.length);
        const param = `queryData=${encodeURIComponent(JSON.stringify(queryData))}`;
        $.ajax({
            url: currentPage.module.server + currentPage.page + "/getPage?" + param,
            headers: { 'Content-Type': 'application/json;charset=utf8', 'JSESSIONID': window.localStorage.getItem('JSESSIONID') },
            type: 'GET',
            dataType: "json",
            async: false,
            success: (result) => {
                layer.msg(result.msg, { icon: result.code + 1, time: 2000 });
                if (result.code === 0) {
                    tableOption.cols = [];
                    tableOption.data = [];
                    // 循环自定义的列属性是为了不改变展示列排序
                    let currentCols = [];
                    for (let key in allCols) {
                        if (result.showColumn.indexOf(key) >= 0) {
                            currentCols.push(allCols[key]);
                        }
                    }
                    // 如果有详情的话
                    if (result.detailGroupBy) {
                        currentCols.push(allCols['detail']);
                        detailGroupBy = result.detailGroupBy;
                    }
                    tableOption.cols.push(currentCols);
                    tableOption.data = result.data;
                    tableOption.totalRow = result.totalRow;
                    tableRender();
                }
            },
            error: (XMLHttpRequest, textStatus) => {
                layer.msg('Ajax Error -> 请求服务器异常，请联系管理员！', { icon: 2, time: 2000 });
            },
            complete: (XMLHttpRequest, textStatus) => {
                return false;
            }
        });
    }

    /**
     * 表格渲染
     */
    function tableRender() {
        // const existRebuildFunction = typeof rebuildTableOption !== "undefined" && rebuildTableOption !== null;
        // existRebuildFunction && rebuildTableOption(tableOption);
        // console.log('tableOption -> ', tableOption)
        //console.log(JSON.stringify(tableOption.data))
        table.render(tableOption);
    }

});
