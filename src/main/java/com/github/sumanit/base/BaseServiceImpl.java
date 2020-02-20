package com.github.sumanit.base;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl <T extends Model,D extends DTOInterface,E extends BaseExample> implements BaseService<T,D,E> {
	/**
	 * slf4j 日志对象 用来记录log
	 */
	private Map<String,String> columnNamePropertyMap = null;
	

	public T selectByPrimaryKey(Object primaryKey) {
		return getMapper().selectByPrimaryKey(primaryKey);
	}

	public T insert(T record) {
		getMapper().insert(record);
		return record;
	}

	public T insertSelective(T record) {
		getMapper().insertSelective(record);
		return record;
	}

	public int deleteByPrimaryKey(Object primaryKey) {
		return getMapper().deleteByPrimaryKey(primaryKey);
	}

	public int updateByPrimaryKey(T record) {
		return getMapper().updateByPrimaryKey(record);
	}

	public int updateByPrimaryKeySelective(T record) {
		return getMapper().updateByPrimaryKeySelective(record);
	}

	public PageInfo<T> pagedQuery(E example, int pageNum, int pageSize) {
		if(pageSize>0){
			PageHelper.startPage(pageNum, pageSize);
			Page<T> result = (Page<T>) getMapper().selectByExample(example);
			PageInfo<T> pageInfo = new PageInfo<T>(result);
			return pageInfo;
		}else{
			List<T> result = (List<T>) getMapper().selectByExample(example);
			Page<T> p = new Page<T>();
			p.addAll(result);
			PageInfo<T> pageInfo = new PageInfo<T>(p);
			return pageInfo;
		}
		
	}

	public long countByExample(E example) {
		return getMapper().countByExample(example);
	}

	public int deleteByExample(E example) {
		return getMapper().deleteByExample(example);
	}

	public List<T> selectByExample(E example) {
		return getMapper().selectByExample(example);
	}

	public int updateByExampleSelective(T record, E example) {
		return getMapper().updateByExampleSelective(record, example);
	}

	public int updateByExample(T record, E example) {
		return getMapper().updateByExample(record, example);
	}

	public List<Object> getIdsByByExample(E example) {
		List<T> list= selectByExample(example);
		List<Object> result = new ArrayList<Object>();
		for(T t : list){
			Object pk = getPKByModel(t);
			if(pk!=null){
				result.add(pk);
			}
		}
		return result;
	}

	public Object getPKByModel(T Model) {
		throw new UnsupportedOperationException();
	}
	
	public String getColumnNameByProperty(String property){
		if(columnNamePropertyMap == null){
			columnNamePropertyMap = getColumnNamePropertyMap();
		}
		return columnNamePropertyMap.get(property);
	}
	
	private Map getColumnNamePropertyMap(){
		Map<String, String> result = new HashMap<String, String>();
		try {
			SqlSession sqlSession = getSqlSessionByMapper(getMapper());
			ResultMap resultMap = sqlSession.getConfiguration().getResultMap(getMapperClassByMapper(getMapper()).getName()+".BaseResultMapRoot");
			List<ResultMapping> propertyResultMappings = resultMap.getPropertyResultMappings();
			for (ResultMapping resultMapping : propertyResultMappings) {
				result.put(resultMapping.getProperty(), resultMapping.getColumn());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
				
	}
	private SqlSession getSqlSessionByMapper(Mapper mapper) throws Exception {
		Object mapperProxy = ((Proxy) mapper).getInvocationHandler(mapper);
        Field sqlSession = mapperProxy.getClass().getDeclaredField("sqlSession");
        sqlSession.setAccessible(true);
        return  (SqlSession) sqlSession.get(mapperProxy);

	}
	private Class getMapperClassByMapper(Mapper mapper) throws Exception {
		Object mapperProxy = ((Proxy) mapper).getInvocationHandler(mapper);
        Field mapperInterface = mapperProxy.getClass().getDeclaredField("mapperInterface");
        mapperInterface.setAccessible(true);
        return  (Class) mapperInterface.get(mapperProxy);
	}	
}

