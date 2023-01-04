package com.example.demo.utils;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.Data;

/**
 * 分类工具类
 * Created by YuanJW on 2022/11/7.
 */
@Data
public class PageUtils {
    /**
     * 起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY = "orderBy";

    /**
     * 排序方向
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 分页参数合理化
     */
    public static final String REASONABLE = "reasonable";

    public static final String ASC = "asc";

    /**
     * 封装分页对象
     * @return
     */
    public static Page getPage() {
        Page page = new Page();
        page.setPageNum(Convert.toInt(ServletUtils.getParameter(PAGE_NUM), 1));
        page.setPageSize(Convert.toInt(ServletUtils.getParameter(PAGE_SIZE), 10));
        if (ServletUtils.getParameter(ORDER_BY) != null) {
            if (ServletUtils.getParameter(IS_ASC) != null) {
                page.setOrderBy(StrUtil.toUnderlineCase(ServletUtils.getParameter(ORDER_BY)) + " " + ServletUtils.getParameter(IS_ASC));
            } else {
                page.setOrderBy(StrUtil.toUnderlineCase(ServletUtils.getParameter(ORDER_BY)) + " " + ASC);
            }
        }
        page.setReasonable(Convert.toBool(ServletUtils.getParameter(REASONABLE)));
        return page;
    }

    public static void startPage() {
        Page page = getPage();
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), page.getOrderBy())
                .setReasonable(page.getReasonable());
    }
}
