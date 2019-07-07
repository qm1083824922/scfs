// package com.scfs.web.controller.base;
//
// import java.util.List;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseBody;
//
// import com.scfs.common.consts.BaseUrlConsts;
// import com.scfs.domain.base.region.dto.resp.DictRegionResDto;
// import com.scfs.service.region.DictRegionService;
//
/// **
// * <pre>
// *
// * File: DictRegionController.java
// * Description:
// * TODO
// * Date, Who,
// * 2016年10月6日 Administrator
// *
// * </pre>
// */
// @Controller
// public class DictRegionController
// {
//
// @Autowired
// private DictRegionService dictRegionService;
//
// @RequestMapping(value = BaseUrlConsts.QUERYREGIONS,method =
// RequestMethod.POST)
// @ResponseBody
// public List<DictRegionResDto> queryRegions() {
// return dictRegionService.queryRegions();
// }
//
// @RequestMapping(value = BaseUrlConsts.QUERYREGIONSBYID,method =
// RequestMethod.POST)
// @ResponseBody
// public DictRegionResDto queryRegionsById(int regionId) {
// return dictRegionService.queryRegionsById(regionId);
// }
// }
//
