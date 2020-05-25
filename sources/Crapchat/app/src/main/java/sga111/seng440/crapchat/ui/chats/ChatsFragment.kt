package sga111.seng440.crapchat.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import sga111.seng440.crapchat.R

class ChatsFragment : Fragment() {

    private lateinit var chatsViewModel: ChatsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        chatsViewModel = ViewModelProvider(this).get(ChatsViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_chats, container, false)
        return view
    }
}
