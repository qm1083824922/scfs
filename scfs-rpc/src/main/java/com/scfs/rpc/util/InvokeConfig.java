package com.scfs.rpc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016年11月24日.
 */
@Component("InvokeConfig")
public class InvokeConfig {
	@Value("${scfs.key}")
	public String SCFS_KEY;
	@Value("${pms.key}")
	public String PMS_KEY;
	@Value("${validateSupplier.url}")
	public String VALIDATESUPPLIER_URL;
	@Value("${uploadSupplierInfo.url}")
	public String UPLOADSUPPLIERINFO_URL;
	@Value("${syncPayOrderInfo.url}")
	public String SYNCPAYORDERINFO_URL;
	@Value("${syncPurchasePass.url}")
	public String SYNCPURCHASEPASS_URL;

	@Value("${cms.key}")
	public String CMS_KEY;
	@Value("${syncCmsPayOrder.url}")
	public String SYNCCMSPAYORDER_URL;
	@Value("${cms.token}")
	public String CMS_TOKEN;
	@Value("${cms.sn}")
	public String CMS_SN;

	@Value("${profile}")
	public String profile;
}
