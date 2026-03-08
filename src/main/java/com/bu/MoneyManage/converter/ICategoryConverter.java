package com.bu.MoneyManage.converter;

import com.bu.MoneyManage.entity.Category;
import com.bu.MoneyManage.vo.CategoryVO;

public interface ICategoryConverter {
    CategoryVO convertEntityToVO(Category category,CategoryVO categoryVO);
}
