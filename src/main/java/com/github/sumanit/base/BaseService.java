package com.github.sumanit.base;

import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * Service接口的基本接口，所有Service接口必须继承
 * @author suman
 *
 * @param <T> 操作对应的model
 */
public interface BaseService<T extends Model,D extends DTOInterface,E extends BaseExample>{
	/**
	 * 获取service对应的Mapper来进行操作
	 * @return
	 */
	abstract Mapper<T,E> getMapper();
	
	/**
	 * 通过查询条件获取满足条件的数据数量
	 * @param example 查询条件
	 * @return 满足条件的数据数量
	 */
	long countByExample(E example);

	/**
	 * 通过条件删除数据
	 * @param example 条件
	 * @return 删除数量
	 */
    int deleteByExample(E example);
    
    /**
     * 通过主键删除数据
     * @param key 主键
     * @return 删除数量
     */
    int deleteByPrimaryKey(Object key);
    
    /**
     * 将数据插入数据库</br>
     * 所有字段都插入数据库不管是不是null
     * @param record 需要插入数据库的对象
     * @return
     */
    T insert(T record);
    
   /**
    * 将数据插入数据库</br>
    * 将所有非null字段都插入数据库
    * @param record
    * @return
    */
    T insertSelective(T record);
    
    /**
     * 通过查询条件查询数据
     * @param example 查询条件
     * @return 数据对象列表
     */
    List<T> selectByExample(E example);
    
    /**
     * 通过主键查询数据
     * @param key 主键
     * @return 数据对象
     */

    T selectByPrimaryKey(Object key);

    /**
     * 通过条件更新数据</br>
     * 只更新非null字段
     * @param record 需要更新的内容
     * @param example 更新的条件
     * @return 更新的数据数量
     */
    int updateByExampleSelective(T record, E example);
    
    /**
     * 通过条件更新数据</br>
     * 更新所有字段 
     * @param record 需要更新的内容
     * @param example 更新的条件
     * @return 更新的数据数量
     */
    int updateByExample(T record, E example);
    
    
    /**
     * 通过主键更新对象</br>
     * 只更新非null字段
     * @param record 需要更新的内容及主键 主键不可为空
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(T record);
    /**
     * 通过主键更新对象</br>
     * 更新所有字段
     * @param record 需要更新的内容及主键 主键不可为空
     * @return 更新的数量
     */
    int updateByPrimaryKey(T record);
    
    /**
     * 通过查询条件和分页条件查询出一页数据
     * @param example 查询条件
     * @param page 页数
     * @param pageSize 每页数据数量
     * @return 一页数据
     */
	PageInfo pagedQuery(E example, int page, int pageSize);
	
	
	Object getPKByModel(T model);
	
	List getIdsByByExample(E example);
    
	/**
	 * 根据实体属性名称获取对应表字段名称
	 * @param property
	 * @return
	 */
	String getColumnNameByProperty(String property);

	E buildExample(D dto,QueryModel queryModel);

	void buildComplexProperty(E baseExample,List<T> result);
}
