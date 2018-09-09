package com.wechat.core.model.base;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 基础信息
 * @author cpr
 * @since 2016-04-0 09:42
 */
@MappedSuperclass
public class BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
    private String id;
	private Timestamp createTime;//创建时间
	private String remark;//备注
	@Transient
	@JsonIgnore
    private Integer page = 1;
    @Transient
    @JsonIgnore
	private Integer rows = 10;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
    
	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
