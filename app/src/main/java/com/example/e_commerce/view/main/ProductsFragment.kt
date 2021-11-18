package com.example.e_commerce.view.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProductsBinding
import com.example.e_commerce.model.product.Product
import com.example.e_commerce.repository.SHARED_PREF_FILE
import com.example.e_commerce.repository.TOKEN_KEY
import com.example.e_commerce.view.adaptersimport.ProductsRecyclerViewAdapter
import kotlin.math.log


class ProductsFragment : Fragment() {

   //1
    private lateinit var binding: FragmentProductsBinding


 //list for the search bar
    private var allProducts = listOf<Product>()


    /////4
    //calling the adapter
    private lateinit var productAdapter: ProductsRecyclerViewAdapter
    //calling the viewModel
    private val productsViewModel: ProductsViewModel by activityViewModels()

    private lateinit var sharedPref: SharedPreferences
    private lateinit var sharedPrefEditor: SharedPreferences.Editor

    //getting the items in the menu to make them appear only if you're signed in
    private lateinit var logoutItem: MenuItem
    private lateinit var profileItem: MenuItem

    //2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        sharedPref = requireActivity().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
        sharedPrefEditor = sharedPref.edit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //2
        binding = FragmentProductsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        //3
        return binding.root
    }

//3
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    // recycler view and adapter
    productAdapter = ProductsRecyclerViewAdapter(productsViewModel)
    binding.productsRecyclerView.adapter = productAdapter

    observers()
    //-----------------------WE WANT TO CALL THE REQUEST WHEN WE OPEN THE APP-------------
    productsViewModel.callProducts()
    }

    //this function observes the live data in the view model
    fun observers(){
        productsViewModel.productsLiveData.observe(viewLifecycleOwner, {

            binding.productsProgressBar.animate().alpha(0f).setDuration(1000)
            productAdapter.submitList(it)

            //to get the data to the list
            allProducts = it
        })

        productsViewModel.productsErrorLiveData.observe(viewLifecycleOwner, { error ->
            error?.let {   Toast.makeText(requireActivity(), error , Toast.LENGTH_SHORT).show()
                if(error  == "Unauthorized") {

                    findNavController().navigate(R.id.action_productsFragment_to_loginFragment)
                    // clears data from the livedata
                    productsViewModel.productsErrorLiveData.postValue(null)
                } }

        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout_item -> {

                sharedPrefEditor.putString(TOKEN_KEY,"")
                sharedPrefEditor.commit()

                logoutItem.isVisible = false
                profileItem.isVisible=false

                productsViewModel.callProducts()
            }
            R.id.profile_item -> {

                findNavController().navigate(R.id.action_productsFragment_to_profileFragment)
            }
        }

   return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        requireActivity().menuInflater.inflate(R.menu.main_menu,menu)

        val searchItem = menu.findItem(R.id.app_bar_search)
        logoutItem = menu.findItem(R.id.logout_item)
        profileItem = menu.findItem(R.id.profile_item)

        val searchView = searchItem.actionView as androidx.appcompat.widget.SearchView
        val token = sharedPref.getString(TOKEN_KEY,"")
        if (token!!.isEmpty()){
            logoutItem.isVisible =false
            profileItem.isVisible=false

        }

        // search when you click enter
        searchView.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                // to filter data

                productAdapter.submitList(allProducts.filter { it.description.lowercase().contains(query!!.lowercase())})
                return true
            }
// search while writing
            override fun onQueryTextChange(newText: String?): Boolean {

//    productAdapter.submitList(allProducts.filter { it.description.lowercase().contains(newText!!.lowercase())})
                return true
            }

        })


        // for the back button
        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {

                productAdapter.submitList(allProducts)
                return true
            }

        })
    }
}