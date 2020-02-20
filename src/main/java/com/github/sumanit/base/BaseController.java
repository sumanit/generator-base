package com.github.sumanit.base;

import com.github.pagehelper.PageInfo;
import com.github.sumanit.base.action.CreateAction;
import com.github.sumanit.base.action.QueryAction;
import com.github.sumanit.base.action.UpdateAction;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller基类</br>
 * 所有Controller都需要继承自该类
 * 
 * @author suman 2016年8月15日 下午1:59:19
 * @param <T>
 * @param <D>
 * @param <E>
 */

public abstract class BaseController<T extends Model, D extends DTOInterface<T>,E extends BaseExample> {
	protected  static String controllerNote="base";


	/**
	 * 通用添加方法
	 * 
	 * @param d 需要插入数据库的对象
	 * @return
	 */

	@RequestMapping(value="",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value="新建操作" ,notes = "新建操作")
	public JsonResponse<T> add(@Validated(CreateAction.class) @RequestBody D d) {
		getService().insertSelective(d.getModel());
		return new JsonResponse<>(200,true,addSuccessMessage(),d.getModel());
	}
	
	/**
	 * 通用数量方法
	 * 
	 * @param d 查询条件传输对象
	 * @return
	 */
	@RequestMapping(value="/count",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "数量统计",notes = "按照指定查询条件统计满足条件的数据数量")
	public JsonResponse<Long> count(@Validated(QueryAction.class) D d) {
		long result = getService().countByExample(getExample(d,QueryModel.SIMPLE));
		return new JsonResponse<>(200,true,countSuccessMessage(),result);
	}

	/**
	 * 通用更新方法
	 * @param id 需要更新的对象id
	 * @param d 需要更新的对象及内容
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "更新操作",notes = "根据指定id更新指定的数据记录")
	public JsonResponse<T> update(@PathVariable(value="id") String id , @Validated(UpdateAction.class) @RequestBody D d) {
		setPrimaryKey(d,id);
		getService().updateByPrimaryKeySelective(d.getModel());
		return new JsonResponse<>(200,true,updateSuccessMessage(),d.getModel());

	}



	/**
	 * 通用删除方法
	 * 
	 * @param id 需要删除的数据id
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
	 * @param id 需要删除的数据id
	 * @return
	 */
	@RequestMapping(value="/{id}",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询一条记录",notes = "根据指定id查询指定的数据记录")
	public JsonResponse<T> selOne(@PathVariable(value = "id")String id) {
		T t = getService().selectByPrimaryKey(id);
		return new JsonResponse<>(200,true,queryOneSuccessMessage(), t);
		
		
	}

	/**
	 * list
	 * @param t
	 * @param queryModel
	 * @param sortName
	 * @param sortOrder
	 * @return
	 */
	@RequestMapping(value="",method=RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value = "查询操作",notes = "根据指定查询条件查询数据记录集")
	public JsonResponse<List<T>> list(@Validated(QueryAction.class) D t, @ApiParam(value="查询模式") @RequestParam(value = "queryModel",defaultValue = "SIMPLE") QueryModel queryModel, @ApiParam(value="排序字段") @RequestParam(value = "sortName",required = false) String sortName, @ApiParam(value="排序方式") @RequestParam(value = "sortOrder",required = false)String sortOrder) {
		E baseExample = getExample(t,queryModel);
		if(isNotNull(sortOrder)&&isNotNull(sortName)){
			baseExample.setOrderByClause(baseExample.getTableAlias()+"."+sortName+" "+sortOrder);
		}else if (isNotNull(sortName) && !isNotNull(sortOrder)){
			baseExample.setOrderByClause(sortName);
		}

		List<T> result = getService().selectByExample(baseExample);
		if(queryModel == QueryModel.ALL){
			getService().buildComplexProperty(baseExample,result);
		}

		baseExample.wrap(result);
		return new JsonResponse<>(200,true,querySuccessMessage(),result);

	}



	/**
	 * 通用分页查询方法，从数据库中查出一页数据
	 * @param t
	 * @param page
	 * @param pageSize
	 * @param queryModel
	 * @param sortName
	 * @param sortOrder
	 * @return
	 */
	@RequestMapping(value="",method=RequestMethod.GET,params = "page")
	@ResponseBody
	@ApiOperation(value = "分页查询操作",notes = "根据指定查询条件查询分页数据记录")
	public JsonResponse<PageInfo<T>> page(@Validated(QueryAction.class) D t, @ApiParam(defaultValue ="1",value="页码") @RequestParam(value = "page",defaultValue = "1") int page, @ApiParam(defaultValue ="100",value="每页数量") @RequestParam(value = "pageSize",defaultValue="100") int pageSize, @ApiParam(value="查询模式")  @RequestParam(value = "queryModel",defaultValue="SIMPLE") QueryModel queryModel,@ApiParam(value="排序字段") @RequestParam(value = "sortName",required = false)  String sortName, @RequestParam(value = "sortName",required = false)  @ApiParam(value="排序方式")String sortOrder) {
		E baseExample = getExample(t,queryModel);
		if(isNotNull(sortOrder)&&isNotNull(sortName)){
			baseExample.setOrderByClause(baseExample.getTableAlias()+"."+sortName+" "+sortOrder);
		}else if (isNotNull(sortName) && !isNotNull(sortOrder)){
			baseExample.setOrderByClause(sortName);
		}

		PageInfo pageInfo = getService().pagedQuery(baseExample, page, pageSize);
		if(queryModel == QueryModel.ALL){
			getService().buildComplexProperty(baseExample,pageInfo.getList());
		}

		baseExample.wrap(pageInfo.getList());
		return new JsonResponse<>(200,true,queryPageSuccessMessage(pageInfo),pageInfo);

	}


	/**
	 * 获取到执行各项操作需要的service
	 * @return
	 */
	protected abstract BaseService<T,D,E> getService();

	/**
	 * 得到执行查询操作需要的查询条件
	 * @param d
	 * @param queryModel
	 * @return
	 */
	protected E getExample(D d, QueryModel queryModel){
		return getService().buildExample(d,queryModel);
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	private boolean isNotNull(Object object) {
		if (object == null) {
			return false;
		}
		if (object.toString().trim().isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 *
	 * @return
	 */
	protected String addSuccessMessage(){
		return "add success";
	}

	/**
	 *
	 * @return
	 */
	protected String addErrorMessage(){
		return "add error";
	}

	/**
	 *
	 * @return
	 */
	protected String countSuccessMessage(){
		return "query count success";
	}

	/**
	 *
	 * @return
	 */
	protected String updateSuccessMessage(){
		return "update success";
	}

	/**
	 *
	 * @return
	 */
	protected String deleteSuccessMessage(){
		return "delete success";
	}

	/**
	 *
	 * @return
	 */
	protected String deleteErrorMessage(){
		return "delete Error";
	}

	/**
	 *
	 * @return
	 */
	protected String queryOneSuccessMessage(){
		return "query one success";
	}

	/**
	 *
	 * @return
	 */
	protected String querySuccessMessage(){
		return "query success";
	}

	/**
	 *
	 * @param pageInfo
	 * @return
	 */
	protected String queryPageSuccessMessage(PageInfo pageInfo){
		return "query page success";
	}

	/**
	 *
	 * @param model
	 * @param primaryKey
	 */

	protected abstract void setPrimaryKey(D model, String primaryKey);


}
