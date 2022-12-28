package com.test.catlistdio.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.catlistdio.R
import com.test.catlistdio.SynchronyApp
import com.test.catlistdio.models.CatBreedList
import com.test.catlistdio.models.CatBreedListItem
import com.test.catlistdio.ui.adapter.CatBreedListAdapter
import com.test.catlistdio.ui.adapter.ICatBreedListRowClickListener
import com.test.catlistdio.utils.DataCallback
import com.test.catlistdio.viewmodel.CatBreedsListViewModel
import kotlinx.android.synthetic.main.fragment_cat_breeds_list.*
import com.test.catlistdio.ui.HomeActivity


class CatBreedsListFragment : Fragment(), ICatBreedListRowClickListener {
    private lateinit var catBreedListAdapter: CatBreedListAdapter
    private lateinit var catBreedsListViewModel: CatBreedsListViewModel
    private lateinit var noResulttv: TextView
    companion object {
        @JvmStatic
        fun newInstance() =
            CatBreedsListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_cat_breeds_list, container, false)
        noResulttv = view.findViewById<TextView>(R.id.noResulttv)
        initRecyclerView(view)
        onViewModelInit()
        return view
    }


    /**
     * Inisialisasi recyclerview
     * mengatur pengelola tata letak
     * mengatur adaptor
     */
    private fun initRecyclerView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewCatBreeds)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            catBreedListAdapter = CatBreedListAdapter(this@CatBreedsListFragment)
            adapter = catBreedListAdapter
        }
    }

    /**
     * Inisialisasi model tampilan
     */
    private fun onViewModelInit() {
        catBreedsListViewModel = ViewModelProvider(this, object: ViewModelProvider.NewInstanceFactory(){
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CatBreedsListViewModel(context?.applicationContext as SynchronyApp) as T
            }
        }).get(CatBreedsListViewModel::class.java)

        initCatBreedsListObserver()
    }

    /**
     * Daftarkan pengamat untuk mendapatkan pembaruan dari model tampilan
     */
    private fun initCatBreedsListObserver() {
        catBreedsListViewModel.getCatBreedsListObserver().observe(viewLifecycleOwner, object :
            Observer<DataCallback<CatBreedList>> {
            override fun onChanged(t: DataCallback<CatBreedList>?) {

                when(t?.status) {
                    DataCallback.Status.LOADING -> {
                        showProgressBar()
                    }
                    DataCallback.Status.ERROR -> {
                        hideProgressBar()
                        updateError(getString(R.string.common_error_server_error))
                    }
                    DataCallback.Status.SUCCESS -> {
                        hideProgressBar()
                        catBreedListAdapter.setUpdatedData(t.data!!)
                    } else -> {
                        hideProgressBar()
                        updateError(getString(R.string.common_error_server_error))
                    }
                }
            }
        })
        loadCatsBredList()
    }

    /**
     * Lakukan panggilan api untuk mendapatkan daftar ras kucing.
     */
    private fun loadCatsBredList() {
        if(catBreedsListViewModel.hasInternetConnection()) {
            catBreedsListViewModel.getCatBreedsList()
        } else {
            updateError(getString(R.string.common_error_no_internet))
        }
    }

    /**
     * Tampilkan Kesalahan pada UI saat api tidak memberikan hasil atau tidak ada konektivitas internet.
     */
    private fun updateError(error: String) {
        noResulttv.visibility = View.VISIBLE
        noResulttv.text = error
    }

    /**
     * Tampilkan pemintal indikator kemajuan.
     */
    private fun showProgressBar() {
        progressBar_cyclic.visibility = View.VISIBLE
    }

    /**
     * Sembunyikan pemintal indikator kemajuan
     */
    private fun hideProgressBar() {
        progressBar_cyclic.visibility = View.GONE
    }

    /**
     * Tangani klik baris tampilan pendaur ulang.
     */
    override fun onCatBreedRowClick(catBreedListItem: CatBreedListItem) {
        (activity as HomeActivity).openDetailFragment(catBreedListItem)
    }
}