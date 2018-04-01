package com.itheima.bos.domain.take_delivery;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**  
 * ClassName:PageBean <br/>  
 * Function:  <br/>  
 * Date:     2018年3月31日 下午9:21:42 <br/>       
 */
//CXF框架在传输数据的时候,无法对page对象进行转换,因为这个类是spring框架提供的
//无法在这个类上加xmlrootelement注解
//所以需要自己构建一个对象 ,来封装分页查询
@XmlRootElement(name="pageBean")
@XmlSeeAlso(Promotion.class)
public class PageBean<T> {

    private List<T> list;
    private Long total;
    public List<T> getList() {
        return list;
    }
    public void setList(List<T> list) {
        this.list = list;
    }
    public Long getTotal() {
        return total;
    }
    public void setTotal(Long total) {
        this.total = total;
    } 
}
  
