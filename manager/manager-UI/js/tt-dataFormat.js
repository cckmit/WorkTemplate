/**
 * 新增活跃用户解析
 * @param {String} value 参数
 */
function dataFormat(value) {
    let data = 0;
    var valueMap = eval("(" + value + ")");
    for (var key in valueMap)
        data = data + valueMap[key];

    return data;
}
function openTimesFormat(value) {
    let data = 0;
    var valueMap = eval("(" + value + ")");
    for (var key in valueMap)
        data = data + valueMap[key];

    return data;
}
function searchDataFormat(value, relation) {
    let data = 0;
    console.log(relation)
    var valueMap = eval("(" + value + ")");
    for (var key in valueMap) {
        console.log("--:" + key)
        console.log("++:" + relation[key])
        if (relation[key] == "搜索") {
            data = data + valueMap[key]
        }
    }
    return data;
}
function findDataFormat(value, relation) {
    let data = 0;
    var valueMap = eval("(" + value + ")");
    for (var key in valueMap) {
        if (relation[key] == "发现") {
            data = data + valueMap[key]
        }
    }
    return data;
}
function gameBoxDataFormat(value, relation) {
    let data = 0;
    var valueMap = eval("(" + value + ")");
    for (var key in valueMap) {
        if (relation[key] == "游戏盒子") {
            data = data + valueMap[key]
        }
    }
    return data;
}
function otherDataFormat(value, relation) {
    let data = 0;
    var valueMap = eval("(" + value + ")");
    for (var key in valueMap) {
        if (relation[key] == undefined) {
            data = data + valueMap[key]
        }
    }
    return data;
}