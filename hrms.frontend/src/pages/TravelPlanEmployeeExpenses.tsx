import React from 'react'
import Button from '../components/Button'
import { Link, useParams } from 'react-router'
import useEmployeeTravelPlanExpensesQuery from '../query/queryHooks/useEmployeeTravelPlanExpensesQuery'
import UserItem from '../components/UserItem'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import formatDate from '../utils/formatDate'
import { CheckIcon, PencilIcon, XMarkIcon } from '@heroicons/react/24/solid'
import useChangeTravelPlanExpenseStatusMutation from '../query/queryHooks/useChangeTravelPlanExpenseStatusMutation'
import { useAuthorization } from '../hooks/useAuthorization'

const TravelPlanEmployeeExpenses = () => {
  const { travelPlanId, employeeId } = useParams()
  const { isOwner, hasRole } = useAuthorization()
  const { data, isSuccess, isLoading, isError, error } = useEmployeeTravelPlanExpensesQuery(travelPlanId, employeeId)

  const changeTravelPlanExpenseStatus = useChangeTravelPlanExpenseStatusMutation()

  const handleStatusChange = (expenseId: Number, status: string) => {
    let remarks;
    if (status=="Rejected") {
      remarks = window.prompt("Add remarks");
    }
    changeTravelPlanExpenseStatus.mutate({expenseId, data:{status, remarks}})
  }

  if (isLoading) {
    return <Loading />
  }
  if (isError) {
    return <ServerError />
  }
  if (isSuccess) {
    const travellingEmployee = data?.data.employee;
    const travelPlan = data?.data.travelPlan;
    const expenses = data?.data.expenses;
    return (
      <>
        <div>
          <Link
            to={`/travel-plan/${travelPlanId}`}
            className="inline-flex items-center text-sm text-gray-500 hover:text-gray-700"
          >
            ← Back to travel plan
          </Link>
          {isOwner(travellingEmployee.id) && <div className='m-4'>
            <Button to={"add"}>Add</Button>
          </div>}
          <div
            className="text-slate-800 flex w-full items-center rounded-md p-3 transition-all hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100"
          >
            <UserItem employee={travellingEmployee} key={travellingEmployee.id} />
          </div>
          <h1 className='text-2xl font-bold px-4 mt-2'>Expenses for {travelPlan.place} <span className='text-sm text-slate-600'>({travelPlan.startDate} to {travelPlan.endDate})</span></h1>
          <div className="p-4">
            {expenses.map((expense: any) => {
              return <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300 mb-4">
                <div className="p-4 flex justify-between">
                  <div>
                    <h3 className="text-xl mb-2 font-semibold text-gray-900">Amount: {expense.amount}</h3>
                    <>
                      <p className="text-sm text-gray-600">Status: {expense.status}</p>
                      <p className="text-sm text-gray-600">Description: {expense.description}</p>
                      <p className="text-sm text-gray-600">Uploaded: {formatDate(expense?.createdAt, {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric'
                      })}</p>
                      <div className='mt-2'>
                        {expense?.expenseMedias?.map((media: any, index: number) => (
                          <div key={index} className="">
                            <a href={media?.url} className="h-full text-blue-800">{media?.originalName}</a>
                          </div>
                        ))}
                      </div>
                    </>
                  </div>
                  <div className="items-center space-x-2">
                    <div className='flex justify-end mr-0'>
                      <span className={`py-1 text-xs font-medium rounded-full ${expense.status == "Approved" ? 'text-green-600' : expense.status == "Rejected" ? 'text-red-500' : 'text-gray-500'}`}>
                        {expense.status}
                      </span>
                    </div>
                    <div className='flex justify-end mr-0'>
                      
                      {isOwner(travellingEmployee.id) && expense.status == "Draft" && <Link to={`${expense.id}/draft`} className="flex cursor-pointer items-center mt-1 hover:bg-gray-100 p-1 rounded"><PencilIcon className='h-4 w-4 text-gray-500' /></Link>}
                    </div>
                      {hasRole(["HR"]) && expense.status == "Submitted" && 
                        <div className='flex items-center gap-2'>
                          <button onClick={() => handleStatusChange(expense.id, "Approved")} title='Approve' className="hover:bg-gray-100 rounded p-0 cursor-pointer items-center"><CheckIcon className='h-5 w-5 text-green-600' /></button>
                          <button onClick={() => handleStatusChange(expense.id, "Rejected")} title='Reject' className="hover:bg-gray-100 rounded p-0 cursor-pointer items-center"><XMarkIcon className='h-5 w-5 text-red-600' /></button>
                        </div>
                      }
                  </div>
                </div>
              </div>
            }
            )}
            <div className='ml-1'>
              <h1 className='text-lg'>Total expense count: {data.data.totalCount}</h1>
              <h1 className='text-lg'>Total Claimed Amount: {data.data.totalClaimedAmount}</h1>
              <h1 className='text-lg'>Unclaimable Amount: {data.data.totalUnclaimableAmount}</h1>
              <h1 className='text-lg'>Total Amount: {data.data.totalAmount}</h1>
            </div>
          </div>

        </div>
      </>
    )
  }
}

export default TravelPlanEmployeeExpenses
