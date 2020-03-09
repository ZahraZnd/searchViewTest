package com.example.testsearchfragment.ui.main

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.testsearchfragment.R
import com.arlib.floatingsearchview.FloatingSearchView
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter
import com.arlib.floatingsearchview.util.Util
import com.example.testsearchfragment.adapter.SearchResultsListAdapter
import com.example.testsearchfragment.data.ColorSuggestion
import com.example.testsearchfragment.data.ColorWrapper
import com.example.testsearchfragment.data.DataHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main_fragment.*


@Suppress("DEPRECATION")
class MainFragment : Fragment() {
    private val tags = "BlankFragment"

    private val FIND_SUGGESTION_SIMULATED_DELAY: Long = 250

    private var mSearchView: FloatingSearchView? = null

    private var mSearchResultsList: RecyclerView? = null
    private var mSearchResultsAdapter: SearchResultsListAdapter? = null
    var adapter :ArrayAdapter<String>? = null

    private var mIsDarkSearchTheme = false

    private var mLastQuery = ""

    companion object {
        fun newInstance() = MainFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fab = view!!.findViewById<FloatingActionButton>(R.id.fab_nextpage)

        fab.setOnClickListener{
            val text = "Got it"
            val action = MainFragmentDirections.mainTwo(text)
            it.findNavController().navigate(action)
            }

        mSearchView = view?.findViewById(R.id.search_view) as FloatingSearchView
        mSearchResultsList = view?.findViewById(R.id.search_results_list) as RecyclerView

        setupFloatingSearch()
        setupResultsList()

    }
    private fun setupFloatingSearch() {
        mSearchView!!.setOnQueryChangeListener { oldQuery, newQuery ->
            if (oldQuery != "" && newQuery == "") {
                mSearchView!!.clearSuggestions()
            } else {

                //this shows the top left circular progress
                //you can call it where ever you want, but
                //it makes sense to do it when loading something in
                //the background.
                mSearchView?.showProgress()

                var resultm = adapter!!.getFilter().filter(newQuery)

                //simulates a query call to a data source
                //with a new query.


                DataHelper.findSuggestions(
                    activity!!.applicationContext,
                    newQuery,
                    5,
                    FIND_SUGGESTION_SIMULATED_DELAY,
                    object : DataHelper.OnFindSuggestionsListener {

                        override fun onResults(results: List<ColorSuggestion>) {

                            //this will swap the data and
                            //render the collapse/expand animations as necessary

                            mSearchView!!.swapSuggestions(results)

                            //let the users know that the background
                            //process has completed
                            mSearchView!!.hideProgress()
                        }
                    })
            }

        }

        mSearchView!!.setOnSearchListener(object : FloatingSearchView.OnSearchListener {
            override fun onSuggestionClicked(searchSuggestion: SearchSuggestion) {

                val colorSuggestion = searchSuggestion as ColorSuggestion
                DataHelper.findColors(
                    activity!!.applicationContext, colorSuggestion.body,
                    object : DataHelper.OnFindColorsListener {

                        override fun onResults(results: List<ColorWrapper>) {
                            mSearchResultsAdapter!!.swapData(results)
                        }

                    })

                mLastQuery = searchSuggestion.getBody()
            }

            override fun onSearchAction(query: String) {
                mLastQuery = query

                DataHelper.findColors(
                    activity!!.applicationContext, query,
                    object : DataHelper.OnFindColorsListener {

                        override fun onResults(results: List<ColorWrapper>) {
                            mSearchResultsAdapter!!.swapData(results)
                        }

                    })
            }
        })

        mSearchView!!.setOnFocusChangeListener(object : FloatingSearchView.OnFocusChangeListener {
            override fun onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView!!.swapSuggestions(DataHelper.getHistory(activity!!.applicationContext, 3))

            }

            override fun onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView!!.setSearchBarTitle(mLastQuery)

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

            }
        })


        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView!!.setOnBindSuggestionCallback(object :
            SearchSuggestionsAdapter.OnBindSuggestionCallback {
            override fun onBindSuggestion(
                suggestionView: View, leftIcon: ImageView,
                textView: TextView, item: SearchSuggestion, itemPosition: Int
            ) {
                val colorSuggestion = item as ColorSuggestion

                val textColor = if (mIsDarkSearchTheme) "#ffffff" else "#000000"
                val textLight = if (mIsDarkSearchTheme) "#bfbfbf" else "#787878"

                if (colorSuggestion.isHistory) {
                    leftIcon.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.ic_history_black_24dp, null
                        )
                    )

                    Util.setIconColor(leftIcon, Color.parseColor(textColor))
                    leftIcon.setAlpha(.36f)
                } else {
                    leftIcon.setAlpha(0.0f)
                    leftIcon.setImageDrawable(null)
                }

                textView.setTextColor(Color.parseColor(textColor))
                val text = colorSuggestion.body
                    .replaceFirst(
                        mSearchView!!.getQuery(),
                        "<font color=\"" + textLight + "\">" + mSearchView!!.getQuery() + "</font>"
                    )
                textView.setText(Html.fromHtml(text))
            }

        })

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView!!.setOnSuggestionsListHeightChanged(FloatingSearchView.OnSuggestionsListHeightChanged { newHeight ->
            mSearchResultsList!!.setTranslationY(
                newHeight
            )
        })

        /*
         * When the user types some text into the search field, a clear button (and 'x' to the
         * right) of the search text is shown.
         *
         * This listener provides a callback for when this button is clicked.
         */
        mSearchView!!.setOnClearSearchActionListener(FloatingSearchView.OnClearSearchActionListener {

        })
    }

    private fun setupResultsList() {
        mSearchResultsAdapter = SearchResultsListAdapter()
        mSearchResultsList?.adapter = mSearchResultsAdapter
        mSearchResultsList?.layoutManager = LinearLayoutManager(context)
    }


}
