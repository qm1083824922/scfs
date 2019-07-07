package com.scfs.web.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.fi.dto.req.RecReceiptRelReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.resp.RecReceiptRelResDto;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.RecReceiptRelService;
import com.scfs.web.base.BaseJUnitTest;

public class RecReceiptRelTest extends BaseJUnitTest {
	@Autowired
	RecReceiptRelService recReceiptRelService;
	
	@Test
	public void queryRecReceiptRel() {
		RecReceiptRelSearchReqDto recReceiptRelSearchReqDto = new RecReceiptRelSearchReqDto();
		recReceiptRelSearchReqDto.setReceiptId(3);
		PageResult<RecReceiptRelResDto> result = recReceiptRelService.queryRecReceiptRelResultsByCon(recReceiptRelSearchReqDto);
		System.out.println(result.getTotal());
	}

	@Test
	public void addRecReceiptRel() {
		RecReceiptRelReqDto recReceiptRelSearchReqDto = new RecReceiptRelReqDto();
		recReceiptRelSearchReqDto.setReceiptId(1);
		List<RecReceiptRel> relList = new ArrayList<RecReceiptRel>();
		RecReceiptRel recReceiptRel = new RecReceiptRel();
		recReceiptRel.setRecId(1);
		recReceiptRel.setWriteOffAmount(new BigDecimal(10));
		recReceiptRel.setCurrencyType(1);
		relList.add(recReceiptRel);
		RecReceiptRel rec = new RecReceiptRel();
		rec.setRecId(1);
		rec.setWriteOffAmount(new BigDecimal(3));
		rec.setCurrencyType(1);
		relList.add(rec);
		recReceiptRelSearchReqDto.setRelList(relList);
		recReceiptRelService.createRecReceiptRel(recReceiptRelSearchReqDto);
	}

	@Test
	public void updateRecReceiptRelById() {
		RecReceiptRelReqDto recReceiptRelSearchReqDto = new RecReceiptRelReqDto();
		List<RecReceiptRel> relList = new ArrayList<RecReceiptRel>();
		RecReceiptRel recReceiptRel = new RecReceiptRel();
		recReceiptRel.setId(1);
		recReceiptRel.setWriteOffAmount(new BigDecimal(30));
		relList.add(recReceiptRel);
		recReceiptRelSearchReqDto.setRelList(relList);
		recReceiptRelService.updateRecReceiptRelById(recReceiptRelSearchReqDto);
	}
	public static void main(String[] args) {
		BigDecimal one = new BigDecimal(30);
		BigDecimal two = new BigDecimal(10);
		System.out.println(one.subtract(two));
	}
}
