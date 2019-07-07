package com.scfs.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scfs.domain.base.entity.DictRegion;

@Repository
public interface DictRegionDao {
	public List<DictRegion> queryAllDictRegion();

	public List<DictRegion> queryParents();

	public List<DictRegion> queryChildsByParent(int parent_id);
}
