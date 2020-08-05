package com.tools.unzip;

import com.alibaba.fastjson.JSONObject;
import com.tools.ReflectUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;

/**
 * Excel工具类
 */
public class PoiExcelUtils {

    /**
     * 数字格式化
     */
    private static NumberFormat format = NumberFormat.getInstance();
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PoiExcelUtils.class);
    /**
     * 列默认宽度
     */
    private static final int DEFAUL_COLUMN_WIDTH = 4000;

    /**
     * 1.创建 workbook
     *
     * @return HSSFWorkbook
     * @Author : ll. create at 2016年4月14日 上午9:28:27
     */
    private HSSFWorkbook getHSSFWorkbook() {
        LOGGER.info("【创建 workbook】");
        return new HSSFWorkbook();
    }

    /**
     * 2.创建 sheet
     *
     * @param hssfWorkbook HSSFWorkbook
     * @param sheetName    sheet 名称
     * @return HSSFSheet
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private HSSFSheet getHSSFSheet(HSSFWorkbook hssfWorkbook, String sheetName) {
        LOGGER.info("【创建 sheet】sheetName ： " + sheetName);
        return hssfWorkbook.createSheet(sheetName);
    }

    /**
     * 3.写入表头信息
     *
     * @param hssfWorkbook HSSFWorkbook
     * @param hssfSheet    HSSFSheet
     * @param headers      列标题，数组形式
     *                     <p>
     *                     如{"列标题1@beanFieldName1@columnWidth",
     *                     "列标题2@beanFieldName2@columnWidth"
     *                     ,"列标题3@beanFieldName3@columnWidth"}
     *                     </p>
     *                     <p>
     *                     其中参数@columnWidth可选，columnWidth为整型数值
     *                     </p>
     * @param title        标题
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void writeHeader(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, String title) {
        LOGGER.info("【写入表头信息】");

        // 头信息处理
        String[] newHeaders = headersHandler(headers);

        // 初始化标题和表头单元格样式
        HSSFCellStyle titleCellStyle = createTitleCellStyle(hssfWorkbook);
        // 标题栏
        HSSFRow titleRow = hssfSheet.createRow(0);
        titleRow.setHeight((short) 500);
        HSSFCell titleCell = titleRow.createCell(0);
        // 设置标题文本
        titleCell.setCellValue(new HSSFRichTextString(title));
        // 设置单元格样式
        titleCell.setCellStyle(titleCellStyle);

        // 处理单元格合并，四个参数分别是：起始行，终止行，起始行，终止列
        hssfSheet.addMergedRegion(new CellRangeAddress(0, 0, (short) 0, (short) (newHeaders.length - 1)));

        // 设置合并后的单元格的样式
        titleRow.createCell(newHeaders.length - 1).setCellStyle(titleCellStyle);

        // 表头
        HSSFRow headRow = hssfSheet.createRow(1);
        headRow.setHeight((short) 500);
        HSSFCell headCell = null;
        String[] headInfo = null;
        // 处理excel表头
        for (int i = 0, len = newHeaders.length; i < len; i++) {
            headInfo = newHeaders[i].split("@");
            headCell = headRow.createCell(i);
            headCell.setCellValue(headInfo[0]);
            headCell.setCellStyle(titleCellStyle);
            // 设置列宽度
            setColumnWidth(i, headInfo, hssfSheet);
        }
    }

    /**
     * 写入表头信息
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet    {@link HSSFSheet}
     * @param headers      列标题，数组形式
     *                     <p>
     *                     如{"列标题1@beanFieldName1@columnWidth",
     *                     "列标题2@beanFieldName2@columnWidth"
     *                     ,"列标题3@beanFieldName3@columnWidth"}
     *                     </p>
     *                     <p>
     *                     其中参数@columnWidth可选，columnWidth为整型数值
     *                     </p>
     * @param startIndex   起始行索引
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void writeHeader(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, int startIndex) {
        LOGGER.info("【写入表头信息】");

        HSSFCellStyle headerCellStyle = createTitleCellStyle(hssfWorkbook);

        // 表头
        HSSFRow headRow = hssfSheet.createRow(startIndex);
        headRow.setHeight((short) 500);
        HSSFCell headCell = null;
        String[] headInfo = null;
        // 处理excel表头
        for (int i = 0, len = headers.length; i < len; i++) {
            headInfo = headers[i].split("@");
            headCell = headRow.createCell(i);
            headCell.setCellValue(headInfo[0]);
            headCell.setCellStyle(headerCellStyle);
            // 设置列宽度
            setColumnWidth(i, headInfo, hssfSheet);
        }
    }

    /**
     * 头信息校验和处理
     *
     * @param headers 列标题，数组形式
     *                <p>
     *                如{"列标题1@beanFieldName1@columnWidth",
     *                "列标题2@beanFieldName2@columnWidth"
     *                ,"列标题3@beanFieldName3@columnWidth"}
     *                </p>
     *                <p>
     *                其中参数@columnWidth可选，columnWidth为整型数值
     *                </p>
     * @return 校验后的头信息
     * @Author : ll. create at 2016年4月14日 上午9:24:48
     */
    private String[] headersHandler(String[] headers) {
        List<String> newHeaders = new ArrayList<String>();
        for (String string : headers) {
            if (string != null && !string.trim().isEmpty()) {
                newHeaders.add(string);
            }
        }
        int size = newHeaders.size();

        return newHeaders.toArray(new String[size]);
    }

    /**
     * 4.写入内容部分(默认从第三行开始写入)
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet    {@link HSSFSheet}
     * @param headers      列标题，数组形式
     *                     <p>
     *                     如{"列标题1@beanFieldName1@columnWidth",
     *                     "列标题2@beanFieldName2@columnWidth"
     *                     ,"列标题3@beanFieldName3@columnWidth"}
     *                     </p>
     *                     <p>
     *                     其中参数@columnWidth可选，columnWidth为整型数值
     *                     </p>
     * @param dataList     要导出的数据集合
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void writeContent(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, List<JSONObject> dataList) throws Exception {
        writeContent(hssfWorkbook, hssfSheet, headers, dataList, 2);
    }

    /**
     * 4.写入内容部分
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet    {@link HSSFSheet}
     * @param headers      列标题，数组形式
     *                     <p>
     *                     如{"列标题1@beanFieldName1@columnWidth",
     *                     "列标题2@beanFieldName2@columnWidth"
     *                     ,"列标题3@beanFieldName3@columnWidth"}
     *                     </p>
     *                     <p>
     *                     其中参数@columnWidth可选，columnWidth为整型数值
     *                     </p>
     * @param dataList     要导出的数据集合
     * @param startIndex   起始行的索引
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void writeContent(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, List<JSONObject> dataList, int startIndex) throws Exception {
        LOGGER.info("【写入Excel内容部分】");
        // 2015-8-13 增加，当没有数据的时候，把原来抛异常的方式修改成返回一个只有头信息，没有数据的空Excel
        if (dataList == null || dataList.isEmpty()) {
            LOGGER.warn("【没有内容数据】");
            return;
        }
        HSSFRow row = null;
        HSSFCell cell = null;
        // 单元格的值
        Object cellValue = null;
        // 数据写入行索引
        int rownum = startIndex;
        // 单元格样式
        HSSFCellStyle cellStyle = createContentCellStyle(hssfWorkbook);
        // 遍历集合，处理数据
        for (int j = 0, size = dataList.size(); j < size; j++) {
            row = hssfSheet.createRow(rownum);
            for (int i = 0, len = headers.length; i < len; i++) {
                cell = row.createCell(i);
                String key = headers[i].split("@")[1];
                JSONObject values = dataList.get(j);
                cellValue = values.get(key);
                cellValueHandler(cell, cellValue);
                cell.setCellStyle(cellStyle);
            }
            rownum++;
        }
    }

    /**
     * 设置列宽度
     *
     * @param i        列的索引号
     * @param headInfo 表头信息，其中包含了用户需要设置的列宽
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void setColumnWidth(int i, String[] headInfo, HSSFSheet hssfSheet) {
        if (headInfo.length < 3) {
            // 用户没有设置列宽，使用默认宽度
            hssfSheet.setColumnWidth(i, DEFAUL_COLUMN_WIDTH);
            return;
        }
        if (headInfo[2] == null || headInfo[2].trim().isEmpty()) {
            // 使用默认宽度
            hssfSheet.setColumnWidth(i, DEFAUL_COLUMN_WIDTH);
            return;
        }
        // 使用用户设置的列宽进行设置
        hssfSheet.setColumnWidth(i, Integer.parseInt(headInfo[2]));
    }

    /**
     * 单元格写值处理器
     *
     * @param {{@link   HSSFCell}
     * @param cellValue 单元格值
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void cellValueHandler(HSSFCell cell, Object cellValue) {
        // 2015-8-13 修改，判断cellValue是否为空，否则在cellValue.toString()会出现空指针异常
        if (cellValue == null) {
            cell.setCellValue("");
            return;
        }
        if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        } else if (cellValue instanceof Calendar) {
            cell.setCellValue((Calendar) cellValue);
        } else if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
        } else if (cellValue instanceof Integer || cellValue instanceof Long || cellValue instanceof Short || cellValue instanceof Float) {
            cell.setCellValue((Double.parseDouble(cellValue.toString())));
        } else if (cellValue instanceof HSSFRichTextString) {
            cell.setCellValue((HSSFRichTextString) cellValue);
        }
        cell.setCellValue(cellValue.toString());
    }

    /**
     * 创建标题和表头单元格样式
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @return {@link HSSFCellStyle}
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private HSSFCellStyle createTitleCellStyle(HSSFWorkbook hssfWorkbook) {
        LOGGER.info("【创建标题和表头单元格样式】");
        // 单元格的样式
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        // 设置字体样式，改为变粗
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 13);
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 创建内容单元格样式
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @return {@link HSSFCellStyle}
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private HSSFCellStyle createContentCellStyle(HSSFWorkbook hssfWorkbook) {
        LOGGER.info("【创建内容单元格样式】");
        // 单元格的样式
        HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
        // 设置字体样式，改为不变粗
        HSSFFont font = hssfWorkbook.createFont();
        font.setFontHeightInPoints((short) 11);
        cellStyle.setFont(font);
        // 设置单元格自动换行
        cellStyle.setWrapText(true);
        return cellStyle;
    }

    /**
     * 将生成的Excel输出到指定目录
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param filePath     文件输出目录，包括文件名（.xls）
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    private void write2FilePath(HSSFWorkbook hssfWorkbook, String filePath) {
        LOGGER.info("【将生成的Excel输出到指定目录】filePath ：" + filePath);
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filePath);
            hssfWorkbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            LOGGER.error("【将生成的Excel输出到指定目录失败】", e);
            throw new RuntimeException("将生成的Excel输出到指定目录失败");
        }
    }

    /**
     * 生成Excel，存放到指定目录
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param filePath  要导出的Excel存放的文件路径
     * @param headers   列标题，数组形式
     *                  <p>
     *                  如{"列标题1@beanFieldName1@columnWidth",
     *                  "列标题2@beanFieldName2@columnWidth"
     *                  ,"列标题3@beanFieldName3@columnWidth"}
     *                  </p>
     *                  <p>
     *                  其中参数@columnWidth可选，columnWidth为整型数值，默认4000
     *                  </p>
     * @param list      要导出数据的集合
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static void createExcel2FilePath(String sheetName, String title, String filePath, String[] headers, List<JSONObject> list) throws Exception {
        if (headers == null) {
            LOGGER.warn("【表头为空】");
            throw new RuntimeException("表头不能为空");
        }
        LOGGER.info("【生成Excel,并存放到指定文件夹目录下】sheetName : " + sheetName + " , title : " + title + " , filePath : " + filePath + " , headers : " + Arrays.toString(headers));
        //进行文件大小拆分
        BigDecimal fileLimit = BigDecimal.valueOf(100000);
        //进行向上取整
        int fileSize = BigDecimal.valueOf(list.size()).divide(fileLimit, 0, RoundingMode.UP).intValue();
        final int maxLimit = fileSize;
        while (fileSize-- > 0) {
            List<JSONObject> dataList = new ArrayList<>();
            int value = fileLimit.intValue();
            while (value-- > 0) {
                if (list.isEmpty()) {
                    break;
                }
                dataList.add(list.remove(0));
            }
            PoiExcelUtils poiExcelUtil = new PoiExcelUtils();
            // 1.创建 Workbook
            HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();

            //向上取整
            BigDecimal max = BigDecimal.valueOf(50000);
            int dataSize = dataList.size();
            System.out.println("读取文件行数:" + dataSize);
            int size = BigDecimal.valueOf(dataSize).divide(max, 0, BigDecimal.ROUND_UP).intValue();
            for (int i = 0; i < size; i++) {
                int end = (i + 1) * max.intValue();
                if (end >= dataSize) {
                    end = dataSize;
                }
                List<JSONObject> context = dataList.subList(i * max.intValue(), end);
                // 2.创建 Sheet
                HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName + "-" + i);
                // 3.写入 head
                poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, title);
                // 4.写入内容
                poiExcelUtil.writeContent(hssfWorkbook, hssfSheet, headers, context);
            }
            String path = filePath + "-" + (maxLimit - fileSize) + ".xls";
            // 5.保存文件到filePath中
            poiExcelUtil.write2FilePath(hssfWorkbook, path);
        }
    }

    /**
     * 生成Excel，存放到指定目录
     *
     * @param sheetName      sheet名称
     * @param title          标题
     * @param filePath       要导出的Excel存放的文件路径
     * @param mainDataFields 主表数据需要展示的字段集合
     *                       <p>
     *                       如{"字段1@beanFieldName1","字段2@beanFieldName2",字段3@beanFieldName3
     *                       "}
     *                       </p>
     * @param mainData       主表数据
     * @param headers        列标题，数组形式
     *                       <p>
     *                       如{"列标题1@beanFieldName1@columnWidth",
     *                       "列标题2@beanFieldName2@columnWidth"
     *                       ,"列标题3@beanFieldName3@columnWidth"}
     *                       </p>
     *                       <p>
     *                       其中参数@columnWidth可选，columnWidth为整型数值，默认4000
     *                       </p>
     * @param detailDataList 要导出数据的集合
     * @param needExportDate 是否需要显示“导出日期”
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static void createExcel2FilePath(String sheetName, String title, String filePath, String[] mainDataFields, Object mainData, String[] headers, List<JSONObject> detailDataList, final boolean needExportDate) throws Exception {
        if (headers == null) {
            LOGGER.warn("【参数headers为空】");
            throw new IllegalArgumentException("headers");
        }
        if (mainDataFields == null) {
            LOGGER.warn("【参数mainDataFields】");
            throw new IllegalArgumentException("mainDataFields");
        }
        if (mainData == null) {
            LOGGER.warn("【参数mainData】");
            throw new IllegalArgumentException("mainData");
        }
        LOGGER.info("【生成Excel,并存放到指定文件夹目录下】sheetName : " + sheetName + " , title : " + title + " , filePath : " + filePath + " , headers : " + Arrays.toString(headers));

        PoiExcelUtils poiExcelUtil = new PoiExcelUtils();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写标题
        headers = poiExcelUtil.writeTitle(hssfWorkbook, hssfSheet, headers, title);
        // 4.写主表（mainData）数据
        int usedRows = poiExcelUtil.writeMainData(hssfWorkbook, hssfSheet, headers.length, mainDataFields, mainData, 1, needExportDate);
        // 5.写入 head 这里默认将title写入到了第一行，所以header的起始行索引为usedRows + 1
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, usedRows + 1);
        // 6.写从表（detailDataList）内容
        poiExcelUtil.writeContent(hssfWorkbook, hssfSheet, headers, detailDataList, usedRows + 2);
        // 7.保存文件到filePath中
        poiExcelUtil.write2FilePath(hssfWorkbook, filePath);
    }

    /**
     * 写标题
     *
     * @param hssfWorkbook {@link HSSFWorkbook}
     * @param hssfSheet    {@link HSSFSheet}
     * @param headers      表头
     * @param title        标题
     * @return 去除无效表头后的新表头集合
     * @Author : ll. create at 2016年5月23日 上午11:24:08
     */
    private String[] writeTitle(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, String title) {
        return writeTitle(hssfWorkbook, hssfSheet, headers, title, 0);
    }

    /**
     * 写标题
     *
     * @param hssfWorkbook  {@link HSSFWorkbook}
     * @param hssfSheet     {@link HSSFSheet}
     * @param headers       表头
     * @param title         标题
     * @param titleRowIndex 标题行的索引
     * @return 去除无效表头后的新表头集合
     * @Author : ll. create at 2016年5月23日 上午11:24:08
     */
    private String[] writeTitle(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, String[] headers, String title, int titleRowIndex) {
        // 头信息处理
        String[] newHeaders = headersHandler(headers);

        // 初始化标题和表头单元格样式
        HSSFCellStyle titleCellStyle = createTitleCellStyle(hssfWorkbook);
        // 标题栏
        HSSFRow titleRow = hssfSheet.createRow(titleRowIndex);
        titleRow.setHeight((short) 500);
        HSSFCell titleCell = titleRow.createCell(0);
        // 设置标题文本
        titleCell.setCellValue(new HSSFRichTextString(title));
        // 设置单元格样式
        titleCell.setCellStyle(titleCellStyle);

        // 处理单元格合并，四个参数分别是：起始行，终止行，起始列，终止列
        hssfSheet.addMergedRegion(new CellRangeAddress(titleRowIndex, titleRowIndex, (short) 0, (short) (newHeaders.length - 1)));

        // 设置合并后的单元格的样式
        titleRow.createCell(newHeaders.length - 1).setCellStyle(titleCellStyle);

        return newHeaders;
    }

    /**
     * 写主表（mainData）数据
     *
     * @param hssfWorkbook   {@link HSSFWorkbook}
     * @param hssfSheet      {@link HSSFSheet}
     * @param columnSize     列数
     * @param mainDataFields 主表数据需要展示的字段集合
     * @param mainData       主表数据对象
     * @param startIndex     起始行索引
     * @param needExportDate 是否需要输出“导出日期”
     * @return 主表数据使用了多少行
     * @throws Exception
     * @Author : ll. create at 2016年5月23日 上午10:24:58
     */
    private int writeMainData(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, int columnSize, String[] mainDataFields, Object mainData, int startIndex, boolean needExportDate) throws Exception {
        // 1.计算主表数据需要写多少行，每行写多少个单元格，每行写多少个字段
        int fieldsSize = mainDataFields.length;
        // 导出日期是否需要独立一行显示
        boolean exportDateSingleRow = fieldsSize * 2 % columnSize == 0;

        // 主表属性需要的行数
        int needRows = exportDateSingleRow ? fieldsSize * 2 / columnSize : fieldsSize * 2 / columnSize + 1;
        if (needExportDate && exportDateSingleRow) {
            needRows += 1;
        }
        // 主表属性显示时，每行显示的主表属性量
        int filedSizeInOneRow = fieldsSize * 2 < columnSize ? fieldsSize : columnSize / 2;

        // 列数是否为偶数
        final boolean isEvenColumn = columnSize % 2 == 0;

        // // 每个字段需要2个单元格进行展示 --> fieldName : value
        // int fieldsSize = mainDataFields.length;
        // int needCellSize = needExportDate ? (fieldsSize + 1) * 2 : fieldsSize
        // * 2;
        // // 转换列数为偶数
        // final boolean isEvenColumn = columnSize % 2 == 0;
        // int availableColumns = isEvenColumn ? columnSize : columnSize - 1;
        // // 计算写主表数据需要多少行
        // int needRows = needCellSize % availableColumns == 0 ? needCellSize /
        // availableColumns
        // : needCellSize / availableColumns + 1;
        // // 每行显示的字段数
        // int fieldsSizeAddExportDateCell = needExportDate ? fieldsSize + 1 :
        // fieldsSize;
        // int filedSizeInOneRow = fieldsSizeAddExportDateCell % needRows == 0 ?
        // fieldsSizeAddExportDateCell
        // / needRows
        // : fieldsSizeAddExportDateCell / needRows + 1;

        // 2.开始写主表数据
        HSSFRow row = null;
        HSSFCell cell4FiledName = null;
        HSSFCell cell4Value = null;

        // 数据写入行索引
        int rownum = startIndex;
        // 单元格样式
        HSSFCellStyle cellStyle = createContentCellStyle(hssfWorkbook);
        // 每一行的单元格的索引
        int cellIndex = 0;
        // 主表字段的数组索引
        int fieldIndex = 0;
        for (int i = 0; i < needRows; i++) {
            row = hssfSheet.createRow(rownum);
            for (int j = 0; j < filedSizeInOneRow; j++) {
                if (fieldIndex == fieldsSize) {
                    break;
                }
                // 取出对应索引的主表字段，然后切割成字符串数组
                String[] fieldsArray = mainDataFields[fieldIndex].split("@");
                fieldIndex++;
                // 每个字段对应的单元格的索引
                cellIndex = j * 2;
                // 字段描述的单元格
                cell4FiledName = row.createCell(cellIndex);
                cellValueHandler(cell4FiledName, fieldsArray[0]);
                cell4FiledName.setCellStyle(cellStyle);
                // 字段值的单元格
                cell4Value = row.createCell(cellIndex + 1);
                cellValueHandler(cell4Value, ReflectUtils.getValueOfGetIncludeObjectFeild(mainData, fieldsArray[1]));
                cell4Value.setCellStyle(cellStyle);

                // 如果当前行还可以继续写数据，则将导出日期写在该行
                if (fieldIndex == fieldsSize && needExportDate && filedSizeInOneRow != j + 1) {
                    writeExportDate(hssfWorkbook, row, cellIndex + 2);
                    needExportDate = false;
                    hssfSheet.addMergedRegion(new CellRangeAddress(rownum, rownum, (short) cellIndex + 3, (short) (columnSize - 1)));
                    // 设置合并后的单元格的样式
                    setMergedCellStyle(row, cellIndex + 3, columnSize - 1, cellStyle);
                }

                // 如果最后一个有值的单元格后还有空白单元格，将他们合并
                if ((j == filedSizeInOneRow - 1 && !isEvenColumn) || fieldIndex == fieldsSize) {
                    int startCellIndex = needExportDate ? (short) cellIndex + 1 : (short) cellIndex + 3;
                    // 处理单元格合并，四个参数分别是：起始行，终止行，起始列，终止列
                    hssfSheet.addMergedRegion(new CellRangeAddress(rownum, rownum, startCellIndex, (short) (columnSize - 1)));
                    // 设置合并后的单元格的样式
                    setMergedCellStyle(row, startCellIndex, columnSize - 1, cellStyle);
                }
            }

            // 导出日期独自占用一行
            if (needExportDate && fieldIndex == fieldsSize) {
                int exportDateRowNum = rownum + 1;
                row = hssfSheet.createRow(exportDateRowNum);
                writeExportDate(hssfWorkbook, row, 0);
                hssfSheet.addMergedRegion(new CellRangeAddress(exportDateRowNum, exportDateRowNum, 1, (short) (columnSize - 1)));
                // 设置合并后的单元格的样式
                setMergedCellStyle(row, 1, columnSize - 1, cellStyle);
                // 生成一次导出日期之后，改变标识
                needExportDate = false;
                break;
            }

            rownum++;
        }
        return needRows;
    }

    /**
     * 设置合并后的单元格的样式
     *
     * @param row            {@link HSSFRow}
     * @param beginCellIdnex 合并开始的单元格
     * @param endCellIndex   合并结束的单元格
     * @param cellStyle      {@link HSSFCellStyle}
     * @Author : ll. create at 2016年5月23日 下午5:32:12
     */
    private void setMergedCellStyle(HSSFRow row, int beginCellIdnex, int endCellIndex, HSSFCellStyle cellStyle) {
        for (int i = beginCellIdnex + 1; i <= endCellIndex; i++) {
            row.createCell(i).setCellStyle(cellStyle);
        }
    }

    /**
     * 写入导出日期
     *
     * @param row       {@link HSSFRow}
     * @param cellIndex 列索引
     * @Author : ll. create at 2016年5月23日 下午3:13:41
     */
    private void writeExportDate(HSSFWorkbook hssfWorkbook, HSSFRow row, int cellIndex) {
        // 单元格样式
        HSSFCellStyle cellStyle = createContentCellStyle(hssfWorkbook);
        // 导出日期
        HSSFCell cell4ExortDate = row.createCell(cellIndex);
        cellValueHandler(cell4ExortDate, "导出日期");
        cell4ExortDate.setCellStyle(cellStyle);
        // 导出日期的值
        HSSFCell cell4ExportDateValue = row.createCell(cellIndex + 1);
        cellValueHandler(cell4ExportDateValue, new Date());
        cell4ExportDateValue.setCellStyle(cellStyle);
    }

    /**
     * 生成Excel的WorkBook，用于导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param headers   列标题，数组形式
     *                  <p>
     *                  如{"列标题1@beanFieldName1@columnWidth",
     *                  "列标题2@beanFieldName2@columnWidth"
     *                  ,"列标题3@beanFieldName3@columnWidth"}
     *                  </p>
     *                  <p>
     *                  其中参数@columnWidth可选，columnWidth为整型数值，默认4000
     *                  </p>
     * @param dataList  要导出数据的集合
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static HSSFWorkbook createExcel2Export(String sheetName, String title, String[] headers, List<JSONObject> dataList) throws Exception {

        if (headers == null) {
            LOGGER.warn("【表头为空】");
            throw new RuntimeException("表头不能为空");
        }
        LOGGER.info("【生成Excel的WorkBook，用于导出Excel】sheetName : " + sheetName + " , title : " + title + "  , headers : " + Arrays.toString(headers));
        PoiExcelUtils poiExcelUtil = new PoiExcelUtils();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写入 head
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, title);
        // 4.写入内容
        poiExcelUtil.writeContent(hssfWorkbook, hssfSheet, headers, dataList);

        return hssfWorkbook;
    }

    /**
     * 生成Excel的WorkBook，用于导出Excel
     *
     * @param sheetName      sheet名称
     * @param title          标题
     * @param mainDataFields 主表数据需要展示的字段集合
     *                       <p>
     *                       如{"字段1@beanFieldName1","字段2@beanFieldName2",字段3@beanFieldName3
     *                       "}
     *                       </p>
     * @param mainData       主表数据
     * @param headers        列标题，数组形式
     *                       <p>
     *                       如{"列标题1@beanFieldName1@columnWidth",
     *                       "列标题2@beanFieldName2@columnWidth"
     *                       ,"列标题3@beanFieldName3@columnWidth"}
     *                       </p>
     *                       <p>
     *                       其中参数@columnWidth可选，columnWidth为整型数值，默认4000
     *                       </p>
     * @param detailDataList 要导出数据的集合
     * @param needExportDate 是否需要“导出日期”
     * @return {@link HSSFWorkbook}
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static HSSFWorkbook createExcel2Export(String sheetName, String title, String[] mainDataFields, Object mainData, String[] headers, List<JSONObject> detailDataList, boolean needExportDate) throws Exception {
        if (headers == null) {
            LOGGER.warn("【参数headers为空】");
            throw new IllegalArgumentException("headers");
        }
        if (mainDataFields == null) {
            LOGGER.warn("【参数mainDataFields】");
            throw new IllegalArgumentException("mainDataFields");
        }
        if (mainData == null) {
            LOGGER.warn("【参数mainData】");
            throw new IllegalArgumentException("mainData");
        }
        LOGGER.info("【生成Excel,用于导出】sheetName : " + sheetName + " , title : " + title + " , headers : " + Arrays.toString(headers) + " , mainDataFields = " + Arrays.toString(mainDataFields));

        PoiExcelUtils poiExcelUtil = new PoiExcelUtils();
        // 1.创建 Workbook
        HSSFWorkbook hssfWorkbook = poiExcelUtil.getHSSFWorkbook();
        // 2.创建 Sheet
        HSSFSheet hssfSheet = poiExcelUtil.getHSSFSheet(hssfWorkbook, sheetName);
        // 3.写标题
        headers = poiExcelUtil.writeTitle(hssfWorkbook, hssfSheet, headers, title);
        // 4.写主表（mainData）数据
        int usedRows = poiExcelUtil.writeMainData(hssfWorkbook, hssfSheet, headers.length, mainDataFields, mainData, 1, needExportDate);
        // 5.写入 head 这里默认将title写入到了第一行，然后需要header和主表详情间隔一行
        poiExcelUtil.writeHeader(hssfWorkbook, hssfSheet, headers, usedRows + 2);
        // 6.写从表（detailDataList）内容
        poiExcelUtil.writeContent(hssfWorkbook, hssfSheet, headers, detailDataList, usedRows + 3);

        return hssfWorkbook;
    }

    /**
     * 根据文件路径读取excel文件，默认读取第0个sheet
     *
     * @param excelPath   excel的路径
     * @param skipRows    需要跳过的行数
     * @param columnCount 列数量
     * @return List<String [ ]> 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java
     * bean
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount) throws Exception {
        return readExcel(excelPath, skipRows, columnCount, 0, null);
    }

    /**
     * 根据文件路径读取excel文件的指定sheet
     *
     * @param excelPath   excel的路径
     * @param skipRows    需要跳过的行数
     * @param columnCount 列数量
     * @param sheetNo     要读取的sheet的索引，从0开始
     * @return List<String [ ]> 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java
     * bean
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount, int sheetNo) throws Exception {
        return readExcel(excelPath, skipRows, columnCount, sheetNo, null);
    }

    /**
     * 根据文件路径读取excel文件的指定sheet，并封装空值单位各的坐标，默认读取第0个sheet
     *
     * @param excelPath                 excel的路径
     * @param skipRows                  需要跳过的行数
     * @param columnCount               列数量
     * @param noneCellValuePositionList 存储空值的单元格的坐标，每个坐标以x-y的形式拼接，如2-5表示第二行第五列
     * @return List<String [ ]> 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java
     * bean
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount, List<String> noneCellValuePositionList) throws Exception {
        return readExcel(excelPath, skipRows, columnCount, 0, noneCellValuePositionList);
    }

    /**
     * 根据文件路径读取excel文件的指定sheet，并封装空值单位各的坐标，默认读取第0个sheet
     *
     * @param excelPath                           excel的路径
     * @param skipRows                            需要跳过的行数
     * @param columnCount                         列数量
     * @param columnNumberForSkipValueValidateSet 不需要做空值验证的列的索引集合
     * @param noneCellValuePositionList           存储空值的单元格的坐标，每个坐标以x-y的形式拼接，如2-5表示第二行第五列
     * @return List<String [ ]> 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java
     * bean
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount, Set<Integer> columnNumberForSkipValueValidateSet, List<String> noneCellValuePositionList) throws Exception {
        return readExcel(excelPath, skipRows, columnCount, 0, columnNumberForSkipValueValidateSet, noneCellValuePositionList);
    }

    /**
     * 根据文件路径读取excel文件的指定sheet，并封装空值单位各的坐标
     *
     * @param excelPath                 excel的路径
     * @param skipRows                  需要跳过的行数
     * @param columnCount               列数量
     * @param sheetNo                   要读取的sheet的索引，从0开始
     * @param noneCellValuePositionList 存储空值的单元格的坐标，每个坐标以x-y的形式拼接，如2-5表示第二行第五列
     * @return List<String [ ]> 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java
     * bean
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount, int sheetNo, List<String> noneCellValuePositionList) throws Exception {
        return readExcel(excelPath, skipRows, columnCount, sheetNo, null, noneCellValuePositionList);
    }

    /**
     * 根据文件路径读取excel文件的指定sheet，并封装空值单位各的坐标
     *
     * @param excelPath                           excel的路径
     * @param skipRows                            需要跳过的行数
     * @param columnCount                         列数量
     * @param sheetNo                             要读取的sheet的索引，从0开始
     * @param columnNumberForSkipValueValidateSet 不需要做空值验证的列的索引集合
     * @param noneCellValuePositionList           存储空值的单元格的坐标，每个坐标以x-y的形式拼接，如2-5表示第二行第五列
     * @return List<String [ ]> 集合中每一个元素是一个数组，按单元格索引存储每个单元格的值，一个元素可以封装成一个需要的java
     * bean
     * @throws Exception
     * @Author : ll. create at 2016年4月14日 上午9:28:39
     */
    public static List<String[]> readExcel(String excelPath, int skipRows, int columnCount, int sheetNo, Set<Integer> columnNumberForSkipValueValidateSet, List<String> noneCellValuePositionList) throws Exception {
        if (excelPath == null || excelPath.isEmpty()) {
            LOGGER.warn("【参数excelPath为空】");
            return new ArrayList<String[]>();
        }

        FileInputStream is = new FileInputStream(new File(excelPath));
        POIFSFileSystem fs = new POIFSFileSystem(is);
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        List<String[]> list = new ArrayList<String[]>();
        HSSFSheet sheet = wb.getSheetAt(sheetNo);
        // 得到总共的行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        try {
            for (int i = skipRows; i < rowNum; i++) {
                String[] vals = new String[columnCount];
                HSSFRow row = sheet.getRow(i);
                if (null == row) {
                    continue;
                }
                for (int j = 0; j < columnCount; j++) {
                    HSSFCell cell = row.getCell(j);
                    String val = getStringCellValue(cell);

                    // 没有需要跳过校验的列索引
                    if (columnNumberForSkipValueValidateSet.isEmpty()) {
                        if (noneCellValuePositionList != null && (val == null || val.isEmpty())) {
                            // 封装空值单元格的坐标
                            noneCellValuePositionList.add((i + 1) + "-" + j);
                        }
                    } else {
                        // 如果需要校验空值的单元格、当前列索引不在需要跳过校验的索引集合中
                        if (noneCellValuePositionList != null && (val == null || val.isEmpty()) && !columnNumberForSkipValueValidateSet.contains(j)) {
                            // 封装空值单元格的坐标
                            noneCellValuePositionList.add((i + 1) + "-" + j);
                        }
                    }

                    vals[j] = val;
                }
                list.add(vals);
            }
        } catch (Exception e) {
            LOGGER.error("【Excel解析失败】", e);
            throw new RuntimeException("Excel解析失败");
        } finally {
            wb.close();
        }
        return list;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格{@link HSSFCell}
     * @return 单元格数据内容（可能是布尔类型等，强制转换成String）
     * @Author : ll. create at 2016年4月14日 上午9:53:07
     */
    public static String getStringCellValue(HSSFCell cell) {
        if (cell == null) {
            return "";
        }
        String strCell;
        switch (cell.getCellTypeEnum()) {
            case STRING:
                strCell = cell.getStringCellValue();
                break;
            case NUMERIC:
                strCell = String.valueOf(format.format(cell.getNumericCellValue())).replace(",", "");
                break;
            case BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell == null || strCell.isEmpty()) {
            return "";
        }

        return strCell;
    }

}