/**
 * date:2020-05-11
 * author:chen cheng
 * description: 街机模块下拉查询
 * version:1.0.1
 */

/**
 * 渲染下拉框
 * @param {String} selectId 下拉框ID
 * @param {String} moduleName 所属模块名称
 * @param {String} page 查询功能页面
 * @param {String} level 查询级别：windowCache-window对象种的缓存、serverCache-服务器缓存、serverDb-从服务器数据库查询
 */
function renderNormalSelect(selectId, moduleName, page, level) {
    const selectOption = localStorage.getItem("select-option" + moduleName + '-' + page);
    // 如果缓存种有，根据当前级别查询
    if (selectOption) {
        if ('serverDb' === level) {
            getSelectOptionByAjax(selectId, moduleName, page, level);
        } else {
            renderSelect(selectId, selectOption);
        }
    } else {
        getSelectOptionByAjax(selectId, moduleName, page, 'serverCache');
    }
}

/**
 * 通过ajax到服务器查询下拉框选项
 * @param {String} selectId 下拉框ID
 * @param {String} moduleName 所属模块名称
 * @param {String} page 查询功能页面
 * @param {String} level 查询级别：windowCache-window对象种的缓存、serverCache-服务器缓存、serverDb-从服务器数据库查询
 */
function getSelectOptionByAjax(selectId, moduleName, page, level) {
    $.ajax({
        url: window.module[moduleName].server + "/" + moduleName + "/" + page + "/getSelectArray/" + 1111,
        type: 'GET',
        dataType: "json",
        success: (result) => {
            const selectVal = $("#" + selectId).val()
            let selectOption = [];
            selectOption.push('<option value="">全部</option>');
            result.forEach((val, index, arr) => {
                const selected = val.key === selectVal ? 'selected' : '';
                selectOption.push('<option value="' + val.key + '" ' + selected + '>' + val.value + '</option>');
            });
            selectOption = selectOption.join('');
            window.localStorage.setItem("select-option" + moduleName + '-' + page, selectOption);
            renderSelect(selectId, selectOption);
        }
    });
}

/**
 * 渲染下拉框
 * @param {String} selectId 下拉框ID
 * @param {String} selectOption 下拉框选项
 */
function renderSelect(selectId, selectOption) {
    $('#' + selectId).empty();
    $('#' + selectId).append(selectOption);
    form.render();
}
