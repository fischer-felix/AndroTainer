package com.dokeraj.androtainer

import android.os.Bundle
import android.text.util.Linkify
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dokeraj.androtainer.adapter.DockerContainerAdapter
import com.dokeraj.androtainer.globalvars.GlobalApp
import com.dokeraj.androtainer.models.PContainer
import io.noties.markwon.Markwon
import io.noties.markwon.linkify.LinkifyPlugin
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.fragment_docker_lister.*


class DockerListerFragment : Fragment(R.layout.fragment_docker_lister) {
    private val args: DockerListerFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.dis2)

        val globActivity: MainActiviy = (activity as MainActiviy?)!!
        val globalVars: GlobalApp = (globActivity.application as GlobalApp)

        setDrawerInfo(globalVars)


        val hamburgerMenu = ActionBarDrawerToggle(activity,
            drawerMoj,
            toolbarMenu,
            R.string.nav_app_bar_open_drawer_description,
            R.string.navigation_drawer_close)
        val ss = context?.let { ContextCompat.getColor(it, R.color.disText2) }
        hamburgerMenu.drawerArrowDrawable.color = ss!!
        drawerMoj.addDrawerListener(hamburgerMenu)
        hamburgerMenu.syncState()

        // transfer data from login
        val containers: List<PContainer> = args.dContainers.containers


        val nni = (1..100).map { x ->
            PContainer(x.toString(), x.toString(), "trt", "mrt")
        }

        val allContainers = (containers + nni)

        recycler_view.adapter = DockerContainerAdapter(allContainers)
        recycler_view.layoutManager = LinearLayoutManager(activity)
        recycler_view.setHasFixedSize(true)


        btnLogout.setOnClickListener(View.OnClickListener {
            globActivity.invalidateJwt()
            val action = DockerListerFragmentDirections.actionDockerListerFragmentToHomeFragment()
            findNavController().navigate(action)
        })


        btnAbout.setOnClickListener(View.OnClickListener {
            if (tvAboutInfo.visibility == View.VISIBLE)
                tvAboutInfo.visibility = View.INVISIBLE
            else
                tvAboutInfo.visibility = View.VISIBLE

        })

    }

    private fun setDrawerInfo(globalVars:GlobalApp){
        // set the name of the logged in user and the server url
        tvLoggedUsername.text = globalVars.user
        tvLoggedUrl.text = globalVars.url

        //get version name of app
        val pInfo = requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
        val appVersion: String = pInfo.versionName

        // use Markwon to format the text
        val markwon = Markwon.builder(requireContext())
            .usePlugin(LinkifyPlugin.create(Linkify.EMAIL_ADDRESSES or Linkify.WEB_URLS))
            .build()

        // get the text from the string resources and add the version number
        markwon.setMarkdown(tvAboutInfo, getString(R.string.about_app, appVersion))
    }


}