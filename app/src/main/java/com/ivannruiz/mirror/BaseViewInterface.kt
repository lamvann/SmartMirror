package com.ivannruiz.mirror

interface BaseViewInterface {
    /**
     * Optional
     * Set LiveData observers with the ViewModelProvider and the data that needs to be observed
     * Example:
     * obtainViewModel().getPhone().observe(this, Observer<Phone> { phone -> UI Update })
     */
    fun setObservers()

    /**
     * Optional
     * Initializes the UI components necessary for the LifecycleOwner or any View
     */
    fun initUIComponents()

    /**
     * Optional
     * Set listeners for UI components
     * Example:
     * btnNext.setOnClickListener { ViewModel Action or UI update}
     */
    fun setListeners()

    /**
     * Optional
     * Starts an activity, make sure the action is simplified
     */
    fun startActivity(){}

    /**
     * Optional
     * Shows an error message
     * @param errorMessage as [String] the error message to display
     */
    fun showDialog(message: String){}
}
