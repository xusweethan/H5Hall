
package com.m.h5hall.comm.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;


@Table(name = "t_global_banner") 
public class BannerGame {

	@Column(column = "ID")
    public String id;
	
    @Column(column = "ICONURL")
    public String icon;

    @Column(column = "ACTIONPARAMS")
    public String gamekey;
}
