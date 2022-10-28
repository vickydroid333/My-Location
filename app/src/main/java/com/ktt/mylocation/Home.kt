package com.ktt.mylocation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ktt.mylocation.databinding.FragmentHomeBinding
import com.ktt.mylocation.user.MyDataBase
import com.ktt.mylocation.user.MyFactory
import com.ktt.mylocation.user.MyRepository
import com.ktt.mylocation.user.MyViewModel
import kotlinx.coroutines.flow.first

class Home : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var myViewModel: MyViewModel
    private lateinit var loginDataStore: LoginDataStore
    private lateinit var adapter: LocationAdapter

    private var id = "0"
    private var user = "0"
    private var pass = "0"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val myRepository = MyRepository(MyDataBase.getDatabase(requireContext()))
        val factory = MyFactory(myRepository)
        myViewModel = ViewModelProvider(this, factory)[MyViewModel::class.java]
        loginDataStore = LoginDataStore(requireContext())
        lifecycleScope.launchWhenStarted {
            id = loginDataStore.getId().first()
            user = loginDataStore.getUserName().first()
            pass = loginDataStore.getPassword().first()
            Log.i("TAG", "onCreateId:$id")
            Log.i("TAG", "onCreatePass:$pass")
            Log.i("TAG", "onCreateUser:$user")
        }

        updateCurrentLocation()
        getLocaion()
        setRecycle()

        adapter.locationListener = {

            val intent = Intent(requireContext(),MapsActivity::class.java)
            intent.putExtra("lat",it.lat)
            intent.putExtra("lan",it.lan)
            intent.putExtra("id",it.userId)
            startActivity(intent)

        }
    }

    private fun updateCurrentLocation() {
        FusedLocationService.latitudeFlow.observe(viewLifecycleOwner) {
            val lat = it.latitude.toString()
            val longi = it.longitude.toString()
            lifecycleScope.launchWhenStarted {
                Log.i("TAG", "onCreateId:$lat")
                myViewModel.insertLocation(LocationTable(lat, longi, id))
            }
        }
    }

    private fun setRecycle() {
        adapter = LocationAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getLocaion() {
        lifecycleScope.launchWhenStarted {
            myViewModel.getUserLocation(id)!!.collect {

                if (it.isNullOrEmpty()) {
                    Log.i("TAG", "getLocaion: $it")
                    binding.empty.visibility = View.VISIBLE

                } else {
                    try {
                        binding.empty.visibility = View.GONE
                        adapter.differ.submitList(it)
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }
}