package com.buit.commons;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: EntityDao
 * @Description: 基础dao
 * @author 神算子
 * @date 2020年4月26日 下午3:44:47-
 */
public interface EntityDao<E, PK extends Serializable> {
	/**
	 * 根据ID查询
	 * 
	 * @param id
	 * @return
	 */
	public E getById(PK id);

	/**
	 * 根据ID删除
	 * 
	 * @param id
	 */
	public void deleteById(PK id);

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	public void insert(Object entity);

	/**
	 * 更新数据
	 * 
	 * @param entity
	 */
	public void update(Object entity);

	/**
	 * 根据条件删除数据
	 * 
	 * @param entity
	 */
	public void removeByEntity(Object entity);

	/**
	 * 按条件查询方法
	 * 
	 * @param entity
	 * @return
	 */
	public List<E> findByEntity(Object entity);

	/**
	 * 按条件 数数量
	 * 
	 * @param entity
	 * @return
	 */
	public long findByEntityCount(Object entity);

}
