package com.github.sumanit.base;

import com.github.pagehelper.PageInfo;
import com.github.sumanit.base.action.CreateAction;
import com.github.sumanit.base.action.QueryAction;
import com.github.sumanit.base.action.UpdateAction;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller基类<br>
 * 所有Controller都需要继承自该类
 * 
 * @author suman 2016年8月15日 下午1:59:19
 * @param <T>
 */

public abstract class BaseController<O extends Model,T extends DTOInterface<O>,E extends BaseExample> {
	protected  static String controllerNote="base";


	/**
	 * 通用添加方法
	 * 
	 * @param t
	 *            需要插入数据库的对象
	 * @return
	 */

	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="新建操作" ,notes = "新建操作")
	public JsonResponse<T> add(@Validated(CreateAction.class) @RequestBody  T t) {
		getService().insertSelective(t.getModel());
		return new JsonResponse<>(200,true,addSuccessMessage(),t);
	}
	
	/**
	 * 通用数量方法
	 * 
	 * @param t 需要插入数据库的对象的条件
	 * @return
	 */
	@RequestMapping(value="/count",method=RequestMethod.GET)
	@ResponseBody

	@ApiOperation(value = "数量统计",notes = "按照指定查询条件统计满足条件的数据数量")
	public JsonResponse<Integer> count(@Validated(QueryAction.class) T t) {
		int result = getService().countByExample(getExample(t,QueryModel.SIMPLE));
		return new JsonResponse<>(200,true,countSuccessMessage(),result);
	}

	/**
	 * 通用更新方法
	 * 
	 * @param t 需要更新的对象及内容
	 * @return
	 */

	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "更新操作",notes = "根据指定id更新指定的数据记录")
	public JsonResponse<T> update(@PathVariable(value="id") String id , @Validated(UpdateAction.class) @RequestBody T t) {
		setPrimaryKey(t,id);
		getService().updateByPrimaryKeySelective(t.getModel());
		return new JsonResponse<>(200,true,updateSuccessMessage(),t);

	}



	/**
	 * 通用删除方法
	 * 
	 * @param id
	 *            需要删除的数据id
	 * @return
	 */
	@RequestMapping(value = "/{id}",method=RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation(value = "删除操作",notes = "根据指定id删除指定的数据记录")
	public JsonResponse<String> del(@PathVariable(value = "id") String id) {
		if(id == null)
			return  new JsonResponse<>(500,false,deleteErrorMessage(),id);
		int result = getService().deleteByPrimaryKey(id);
		if(result == 0)
			return new JsonResponse<>(500,false,deleteErrorMessage(),id);
		return new JsonResponse<>(500,false,deleteSuccessMessage(),id);
	}
	
	/**
	 * 通用列表查询方法，将数据从数据库中全部查出
	 * saveFile
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询一条记录",notes = "根据指定id查询指定的数据记录")
	public JsonResponse<Model> selOne(@PathVariable(value = "id")String id) {
		Model t = getService().selectByPrimaryKey(id);
		return new JsonResponse<>(200,true,queryOneSuccessMessage(), t);
		
		
	}

	/**
	 * 通用分页查询方法，从数据库中查出一页数据
	 * 
	 * @return
	 */
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询操作",notes = "根据指定查询条件查询数据记录集")
	public JsonResponse<List<O>> list(@Validated(QueryAction.class) T t, @ApiParam(value="查询模式") QueryModel queryModel,@ApiParam(value="排序字段") String sortName, @ApiParam(value="排序方式")String sortOrder) {
		E baseExample = getExample(t,queryModel);
		if(isNotNull(sortOrder)&&isNotNull(sortName)){
			baseExample.setOrderByClause(baseExample.getTableAlias()+"."+sortName+" "+sortOrder);
		}else if (isNotNull(sortName) && !isNotNull(sortOrder)){
			baseExample.setOrderByClause(sortName);
		}

		List<O> result = getService().selectByExample(baseExample);
		return new JsonResponse<>(200,true,querySuccessMessage(),result);

	}
	/**
	 * 通用分页查询方法，从数据库中查出一页数据
	 *
	 * @return
	 */
	@RequestMapping(value="",method=RequestMethod.GET,params = "page")
	@ResponseBody
	@ApiOperation(value = "分页查询操作",notes = "根据指定查询条件查询分页数据记录")
	public JsonResponse<PageInfo> page(@Validated(QueryAction.class) T t, @ApiParam(defaultValue ="1",value="页码") @RequestParam(value = "page",defaultValue = "1") int page,  @ApiParam(defaultValue ="100",value="每页数量") @RequestParam(value = "pageSize",defaultValue="100") int pageSize,@ApiParam(value="查询模式") QueryModel queryModel,@ApiParam(value="排序字段") String sortName, @ApiParam(value="排序方式")String sortOrder) {
		E baseExample = getExample(t,queryModel);
		if(isNotNull(sortOrder)&&isNotNull(sortName)){
			baseExample.setOrderByClause(baseExample.getTableAlias()+"."+sortName+" "+sortOrder);
		}else if (isNotNull(sortName) && !isNotNull(sortOrder)){
			baseExample.setOrderByClause(sortName);
		}

		PageInfo pageInfo = getService().pagedQuery(baseExample, page, pageSize);
		return new JsonResponse<>(200,true,queryPageSuccessMessage(pageInfo),pageInfo);

	}


	/**
	 * 获取到执行各项操作需要的service
	 * 
	 * @return
	 */
	protected abstract BaseService<O,E> getService();

	/**
	 * 得到执行查询操作需要的查询条件
	 * 
	 * @param t
	 *            查询条件存储对象
	 * @return 查询条件对象
	 */
	protected abstract E getExample(T t,QueryModel queryModel);


	private boolean isNotNull(Object object) {
		if (object == null) {
			return false;
		}
		if (object.toString().trim().isEmpty()) {
			return false;
		}
		return true;
	}

	protected String addSuccessMessage(){
		return "add success";
	}
	protected String addErrorMessage(){
		return "add error";
	}

	protected String countSuccessMessage(){
		return "query count success";
	}
	protected String updateSuccessMessage(){
		return "update success";
	}
	protected String deleteSuccessMessage(){
		return "delete success";
	}
	protected String deleteErrorMessage(){
		return "delete Error";
	}
	protected String queryOneSuccessMessage(){
		return "query one success";
	}

	protected String querySuccessMessage(){
		return "query success";
	}
	protected String queryPageSuccessMessage(PageInfo pageInfo){
		return "query page success";
	}

	protected abstract void setPrimaryKey(T model, String primaryKey);


}
