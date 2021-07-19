package com.buit.commons;


import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;


/**
 * 类描述：service基础方法<br>
 * 创建人：文光临<br>
 * 创建时间：2015年4月20日 10:15:42<br>
 * @author 神算子
 */
public abstract class BaseManagerImp<E, PK extends Serializable> {

    /**
     * 设置默认排序
     * @param query
     */
    public void setSortColumns(PageQuery query){
        if(StringUtils.isBlank(query.getSortColumns())){
            query.setSortColumns("GMT_MODIFY DESC");
        }
    }


    /**
     * 取得接口Mapper
     * @return
     */
    public abstract EntityDao<E, PK> getEntityMapper();

    /**
     * 根据主键查询
     */
    public E getById(PK id) {
        return (E) getEntityMapper().getById(id);
    }

    /**
     * 根据条件查询数据
     */
    public List<E> findByEntity(Object entity) {
        return getEntityMapper().findByEntity(entity);
    }

    /**
     * 根据条件删除数据
     */
    public void removeByEntity(Object entity) {
        getEntityMapper().removeByEntity(entity);
    }

    /**
     * 插入数据
     */
    
    public void insert(Object entity) {
        getEntityMapper().insert(entity);
    }

    /**
     * 主键删除数据
     */
    public void removeById(PK id) {
        getEntityMapper().deleteById(id);
    }

    /**
     * 根据条件更新数据
     */
    public void update(Object entity) {

        getEntityMapper().update(entity);
    }




}
