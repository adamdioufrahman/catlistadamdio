package com.test.catlistdio.ui.adapter

import com.test.catlistdio.models.CatBreedListItem

interface ICatBreedListRowClickListener {
    fun onCatBreedRowClick(catBreedListItem: CatBreedListItem)
}