package com.example.creditcardbalancecalculator.ui.monthlytransaction

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TableLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.creditcardbalancecalculator.R
import com.example.creditcardbalancecalculator.data.Constant
import com.example.creditcardbalancecalculator.data.MonthlyTransactionList
import com.example.creditcardbalancecalculator.data.db.entity.MonthlyTransaction
import com.example.creditcardbalancecalculator.helper.MonthlyTransactionHelper
import com.example.creditcardbalancecalculator.helper.MonthlyTransactionHelper.Companion.validateMonthlyTransactionFields
import com.example.creditcardbalancecalculator.task.ExecuteMonthlyTransactionDBTask
import com.example.creditcardbalancecalculator.ui.dialog.CreateEditMonthlyTransactionDialog

class MonthlyTransactionFragment : Fragment(), CreateEditMonthlyTransactionDialog.NoticeDialogListener {
    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        description: String,
        amount: String,
        date: String
    ) {
        val createEditMonthlyTransactionDialog = dialog as CreateEditMonthlyTransactionDialog
        var message = "Monthly transaction created successfully!"
        if (createEditMonthlyTransactionDialog.mode == Constant.CREATE)
            message = createMonthlyTransaction(description, amount, date)
        else
            message = editMonthlyTransactionDialog(description, amount, date)

        val toast =
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            )
        toast.show()
        dialog.dismiss()
        getMonthlyTransactions()
    }

    override fun onDialogSetViewModel(dialog: CreateEditMonthlyTransactionDialog) {
        if (dialog.mode == Constant.EDIT) {
            var selectedTransaction =
                ExecuteMonthlyTransactionDBTask<MonthlyTransaction>(context!!.applicationContext).execute(
                    MonthlyTransactionHelper.getTransaction(selectedRow.tag.toString().toInt())
                ).get()
            dialog.viewModel.description.value = selectedTransaction.description
            dialog.viewModel.date.value = selectedTransaction.date
            dialog.viewModel.amount.value = selectedTransaction.amount
        }
    }

    private fun editMonthlyTransactionDialog(description: String, amount: String, date: String) : String {
        val message = validateMonthlyTransactionFields(description, amount, date)
        return if (message.isEmpty()) {
            var monthlyTransaction = MonthlyTransaction(selectedRow.tag.toString().toInt(), date.toInt(), description, amount.toLong())
            var updateMonthlyTransactionFun = MonthlyTransactionHelper.updateTransaction(monthlyTransaction)
            ExecuteMonthlyTransactionDBTask<Unit>(context!!.applicationContext).execute(
                updateMonthlyTransactionFun
            ).get()
            "Monthly transaction updated successfully!"
        } else {
            message
        }
    }

    private fun createMonthlyTransaction(
        description: String,
        amount: String,
        date: String
    ) :String {
        val message = validateMonthlyTransactionFields(description, amount, date)
        return if (message.isEmpty()) {
            var monthlyTransaction = MonthlyTransaction(date.toInt(), description, amount.toLong())
            var addMonthlyTransactionFun = MonthlyTransactionHelper.saveTransaction(monthlyTransaction)
            ExecuteMonthlyTransactionDBTask<Unit>(context!!.applicationContext).execute(
                addMonthlyTransactionFun
            ).get()
            "Monthly transaction created successfully!"
        } else {
            message
        }
    }


    private lateinit var monthlyTransactionViewModel: MonthlyTransactionViewModel
    private lateinit var transactionTable: TableLayout
    private lateinit var transactionToTableRowPopulator: MonthlyTransactionToTableRowPopulator
    private lateinit var createBtn: Button
    private lateinit var root: View
    private lateinit var selectedRow : View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setViewModel()
        setObserverForLiveData()
        setRoot(inflater, container)
        setViews()
        getMonthlyTransactions()
        handleCreateBtn()
        return root
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        selectedRow = v!!
        val inflater : MenuInflater = activity!!.menuInflater
        inflater.inflate(R.menu.monthly_transaction_menu, menu)
    }
    private fun handleCreateBtn() {
        createBtn.setOnClickListener { _ ->
            var createDialog = CreateEditMonthlyTransactionDialog(Constant.CREATE)
            createDialog.setTargetFragment(this, 0)
            createDialog.show(fragmentManager, "createMonthlyTransactionDialog")
        }

    }

    private fun getMonthlyTransactions() {
        monthlyTransactionViewModel.monthlyTransactionList.value =
            ExecuteMonthlyTransactionDBTask<MonthlyTransactionList>(context!!.applicationContext).execute(
                MonthlyTransactionHelper.getAllTransaction
            ).get()
    }

    private fun setViewModel() {
        monthlyTransactionViewModel =
            ViewModelProviders.of(this).get(MonthlyTransactionViewModel::class.java)
    }

    private fun setObserverForLiveData() {
        // Create the observer which updates the UI.
        val monthlyTransactionObserver =
            Observer<MonthlyTransactionList> { monthlyTransactionList ->
                populateTransactionList(monthlyTransactionList)
            }
        monthlyTransactionViewModel.monthlyTransactionList.observe(this, monthlyTransactionObserver)

    }

    private fun populateTransactionList(monthlyTransactionList: MonthlyTransactionList) {
        clearTransactionTable()
        if (!this::transactionToTableRowPopulator.isInitialized)
            transactionToTableRowPopulator =
                MonthlyTransactionToTableRowPopulator(
                    this,
                    context,
                    transactionTable,
                    monthlyTransactionList
                )

        transactionToTableRowPopulator.monthlyTransactionList = monthlyTransactionList
        transactionToTableRowPopulator.populateDataToMonthlyTransactionTable()
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.edit -> {
                showEditMonthlyTransactionDialog()
                getMonthlyTransactions()
                true
            }
            R.id.delete -> {
                deleteMonthlyTransaction()
                getMonthlyTransactions()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun deleteMonthlyTransaction() {
        ExecuteMonthlyTransactionDBTask<Unit>(context!!.applicationContext).execute(
            MonthlyTransactionHelper.deleteTransaction(selectedRow.tag.toString().toInt())
        )
    }

    private fun showEditMonthlyTransactionDialog() {

        var editDialog = CreateEditMonthlyTransactionDialog(Constant.EDIT)
        editDialog.setTargetFragment(this, 0)
        editDialog.show(fragmentManager, "editMonthlyTransactionDialog")

    }

    private fun setRoot(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        root = inflater.inflate(R.layout.fragment_monthly_transaction, container, false)
    }

    private fun setViews() {
        transactionTable = root.findViewById(R.id.transactionTable)
        createBtn = root.findViewById(R.id.createMonthlyTransactionBtn)
    }

    private fun clearTransactionTable() {
        while (transactionTable.childCount > 1) {
            var rowToRemove = transactionTable.getChildAt(1)
            transactionTable.removeView(rowToRemove)
        }
    }

}