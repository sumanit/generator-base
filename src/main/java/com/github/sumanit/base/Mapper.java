package com.github.sumanit.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 
 * @author suman
 * 2016年8月15日 下午1:52:06
 * @param <T>
 */
public interface Mapper<T extends Model,E extends BaseExample> {
	
	/**
	 * 通过查询条件获取满足条件的数据数量
	 * @param example 查询条件
	 * @return 满足条件的数据数量
	 */
    int countByExample(E example);
    
    /**
	 * 通过条件删除数据
	 * @param example 条件
	 * @return 删除数量
	 */
    int deleteByExample(E example);
    
    /**
     * 通过主键删除数据
     * @param primaryKey 主键
     * @return 删除数量
     */
    int deleteByPrimaryKey(Object primaryKey);

    /**
     * 将数据插入数据库<br>
     * 所有字段都插入数据库不管是不是null
     * @param record 需要插入数据库的对象
     * @return
     */
    int insert(T record);

    /**
     * 将数据插入数据库<br>
     * 将所有非null字段都插入数据库
     * @param record
     * @return
     */
    int insertSelective(T record);
    
    /**
     * 通过查询条件查询数据
     * @param example 查询条件
     * @return 数据对象列表
     */
    
    List<T> selectByExample(E example);

    /**
     * 通过主键查询数据
     * @param primaryKey 主键
     * @return 数据对象
     */
    T selectByPrimaryKey(Object primaryKey);

    
    /**
     * 通过条件更新数据<br>
     * 更新所有字段 
     * @param record 需要更新的内容
     * @param example 更新的条件
     * @return 更新的数据数量
     */
    int updateByExampleSelective(@Param("record") T record, @Param("example") E example);

    /**
     * 通过条件更新数据<br>
     * 更新所有字段 
     * @param record 需要更新的内容
     * @param example 更新的条件
     * @return 更新的数据数量
     */
    int updateByExample(@Param("record") T record, @Param("example") E example);

    /**
     * 通过主键更新对象<br>
     * 只更新非null字段
     * @param record 需要更新的内容及主键 主键不可为空
     * @return 更新的数量
     */
    int updateByPrimaryKeySelective(T record);

    /**
     * 通过主键更新对象<br>
     * 更新所有字段
     * @param record 需要更新的内容及主键 主键不可为空
     * @return 更新的数量
     */
    int updateByPrimaryKey(T record);
}
