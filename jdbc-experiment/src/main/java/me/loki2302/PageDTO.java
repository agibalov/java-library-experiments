package me.loki2302;

import java.util.List;

public class PageDTO<TItem> {
	public int pageNumber;
	public int totalItems;
	public List<TItem> items;
}