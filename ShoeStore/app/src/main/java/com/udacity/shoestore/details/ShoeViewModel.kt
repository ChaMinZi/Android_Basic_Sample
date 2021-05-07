package com.udacity.shoestore.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.shoestore.models.Shoe
import timber.log.Timber

class ShoeViewModel : ViewModel() {

    private val _shoes = MutableLiveData<MutableList<Shoe>>(mutableListOf())
    val shoes: LiveData<MutableList<Shoe>>
        get() = _shoes

    private val _isSaveDetail = MutableLiveData<Boolean>()
    val isSaveDetail : LiveData<Boolean> get() = _isSaveDetail

    init {
        val sampleShoe = Shoe("Name", 47.5, "Company", "Description")
        val sampleShoe2 = Shoe("Name2", 41.5, "Company2", "Description2")
        _shoes.value!!.add(sampleShoe)
        _shoes.value!!.add(sampleShoe2)
    }

    fun emptyShoe(): Shoe{
        return Shoe("", 0.0, "", "")
    }

    fun onSaveClick(shoe: Shoe) {
        Timber.e("onSaveClicked")
        _shoes.value!!.add(shoe)
        _isSaveDetail.value = true
    }

    fun onSaveComplete() {
        _isSaveDetail.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}