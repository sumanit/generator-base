package com.github.sumanit.base;

import java.util.*;


/**
 * 所有Example都需要继承自该类</br>
 * 该类自动生成不需要修改
 * @author suman
 * 2016年8月15日 下午1:58:40
 */
public abstract class BaseExample<T extends BaseExample.GeneratedCriteria> {
    protected String orderByClause;

    protected String groupByClause;
    
    protected boolean distinct;

    protected boolean ignoreCase;

    protected String tableName;

    protected String tableAlias;

    protected List<BaseExample.GeneratedCriteria> oredCriteria;

    protected Map<String, ColumnContainerBase> columnContainerMap;

    protected Set<String> leftJoinTableSet;

    public BaseExample() {
        oredCriteria = new ArrayList<>();
        columnContainerMap = new HashMap<>();
        leftJoinTableSet = new HashSet<>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        if(orderByClause == null){
           return getTableAlias()+".id";
        }
        return orderByClause;
    }

    public void setGroupByClause(String groupByClause) {
        this.groupByClause = groupByClause;
    }

    public String getGroupByClause() {
        return groupByClause;
    }


    public String getTableName() {
        return tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
        oredCriteria.forEach(item ->{
            item.setIgnoreCase(this.ignoreCase);
        });

    }

    public List<GeneratedCriteria> getOredCriteria() {
        if(oredCriteria == null || oredCriteria.isEmpty()){
            GeneratedCriteria criteriaInternal = createCriteriaInternal();
            criteriaInternal.addCriterion("1=1");
            oredCriteria.add(criteriaInternal);
        }else if(oredCriteria.size()==1){
            GeneratedCriteria generatedCriteria = oredCriteria.get(0);
            generatedCriteria.addCriterion("1=1");
        }
        return oredCriteria;
    }

    public Set<ColumnContainerBase> getColumnContainerSet() {
        if(columnContainerMap.size()==0){
            columnContainerMap.put(getTableName(), createColumns());
        }
        return new HashSet(columnContainerMap.values());
    }


    public Set<String> getLeftJoinTableSet() {
        return leftJoinTableSet;
    }

    public T or() {
        T criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public void or(GeneratedCriteria criteria) {
        oredCriteria.add(criteria);
        if(!criteria.getTableName().equals(getTableName())){
            leftJoinTableSet.add(criteria.getTableName());
        }
    }

    public T or(String orSql){
        T criteria = createCriteria();
        criteria.addCriterion(orSql);
        return criteria;
    }

    public GeneratedCriteria and(GeneratedCriteria criteria) {
        GeneratedCriteria oldCriteria =  criteria;
        if(oredCriteria.size()<=0){
            oredCriteria.add(criteria);
        }else{
            oldCriteria = oredCriteria.get(oredCriteria.size()-1);
            oldCriteria.getCriteria().addAll(criteria.getCriteria());
        }
        if(!criteria.getTableName().equals(getTableName())){
            leftJoinTableSet.add(criteria.getTableName());
        }
        return oldCriteria;
    }

    public GeneratedCriteria and(String andSql) {
        GeneratedCriteria criteria = null;
        if(oredCriteria.size()<=0){
            criteria = createCriteria();
        }else{
            criteria = oredCriteria.get(oredCriteria.size()-1);
        }
        criteria.addCriterion(andSql);
        return criteria;
    }

    public T createCriteria(){
        T criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected abstract T createCriteriaInternal();

    protected abstract ColumnContainerBase createColumns();

    public void clear() {
        oredCriteria.clear();
        columnContainerMap.clear();
        leftJoinTableSet.clear();
        orderByClause = null;
        groupByClause = null;
        distinct = false;
        ignoreCase = false;
    }

    public abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;
        protected boolean ignoreCase = false;
        private String tableName;

        public boolean isIgnoreCase() {
            return ignoreCase;
        }
        public boolean isValid() {
            return criteria.size() > 0;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            this.criteria.forEach(item->{
                item.setIgnoreCase(ignoreCase);
            });
        }

        protected GeneratedCriteria(String tableName) {
            super();
            this.criteria = new ArrayList<Criterion>();
            this.tableName = tableName;
        }
        protected GeneratedCriteria(String tableName,boolean ignoreCase) {
            this(tableName);
            this.ignoreCase = ignoreCase;
        }

        public List<Criterion> getCriteria() {

            return criteria;
        }

        public void setCriteria(List<Criterion> criteria){
            this.criteria = criteria;
        }
        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public void setAllCriteria(List<Criterion> criteria) {
            this.criteria = criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value,ignoreCase));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }
        protected void addCriterionForJDBCTime(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value.getTime()), property);
        }

        protected void addCriterionForJDBCTime(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Time> timeList = new ArrayList<java.sql.Time>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                timeList.add(new java.sql.Time(iter.next().getTime()));
            }
            addCriterion(condition, timeList, property);
        }

        protected void addCriterionForJDBCTime(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Time(value1.getTime()), new java.sql.Time(value2.getTime()), property);
        }
        public String getTableName() {
            return tableName;
        }
    }

    public static class Criterion {

        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        private boolean ignoreCase;

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition){
            this.condition = condition;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value){
            this.value = value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public void setNoValue(boolean noValue) {
            this.noValue = noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public void setSingleValue(boolean singleValue){
            this.singleValue = singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public void setListValue(boolean listValue){
            this.listValue = listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        public boolean isIgnoreCase() {
            return ignoreCase;
        }

        public void setIgnoreCase(boolean ignoreCase) {
            this.ignoreCase = ignoreCase;
            if(ignoreCase && value instanceof String){
                String[] conditions = condition.split(" ");
                this.condition = "\"upper\"("+conditions[0]+") " + conditions[1];
                this.value = String.valueOf(value).toUpperCase();
            }
        }

        public Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }


        public Criterion(String condition, Object value, String typeHandler) {
          this(condition,value,typeHandler,false);
        }
        public Criterion(String condition, Object value, String typeHandler,boolean ignoreCase) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
            if(ignoreCase && value instanceof String){
                String[] conditions = condition.split(" ");
                this.condition = "\"upper\"("+conditions[0]+") " + conditions[1];
                this.value = String.valueOf(value).toUpperCase();
            }

        }

        public Criterion(String condition, Object value) {
            this(condition,value,false);
        }

        public Criterion(String condition, Object value,boolean ignoreCase) {
            this(condition, value, null,ignoreCase);
        }

        public Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        public Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }

    protected static class ColumnContainerBase {
        private StringBuffer columnContainerStr;

        private String tableName;

        protected ColumnContainerBase(String tableName) {
            super();
            columnContainerStr = new StringBuffer();
            this.tableName = tableName;
        }

        public boolean isValid() {
            return columnContainerStr.length() > 0;
        }

        public StringBuffer getAllColumn() {
            return columnContainerStr;
        }

        public StringBuffer getColumnContainerStr() {
            return columnContainerStr;
        }

        public void addColumnStr(String column) {
            if(columnContainerStr.toString().indexOf(column)!=-1){
            	return;
            }
            if (columnContainerStr.length() > 0) {
                columnContainerStr.append(",");
            }
            columnContainerStr.append(column);
        }

        public String getTableName() {
            return tableName;
        }
    }
    public  void wrap(List values){};
    public void setComplexProperty(String property,List values){};
}