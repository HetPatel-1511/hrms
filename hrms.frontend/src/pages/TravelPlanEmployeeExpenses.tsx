import React from 'react'
import Button from '../components/Button'
import { Link, useParams } from 'react-router'
import useEmployeeTravelPlanExpensesQuery from '../query/queryHooks/useEmployeeTravelPlanExpensesQuery'
import UserItem from '../components/UserItem'
import Loading from '../components/Loading'
import ServerError from '../components/ServerError'
import formatDate from '../utils/formatDate'

const TravelPlanEmployeeExpenses = () => {
  const { travelPlanId, employeeId } = useParams()
  const { data, isSuccess, isLoading, isError, error } = useEmployeeTravelPlanExpensesQuery(travelPlanId, employeeId)

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
        <div className='m-4'>
          <Button to={"add"}>Add</Button>
        </div>
          <div
            className="text-slate-800 flex w-full items-center rounded-md p-3 transition-all hover:bg-slate-100 focus:bg-slate-100 active:bg-slate-100"
          >
            <UserItem travellingEmployee={travellingEmployee} key={travellingEmployee.id} />
          </div>
          <h1 className='text-2xl font-bold px-4 mt-2'>Expenses for {travelPlan.place} <span className='text-sm text-slate-600'>({travelPlan.startDate} to {travelPlan.endDate})</span></h1>
          <div className="p-4">
            {expenses.map((expense: any) => {
              return <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-shadow duration-300 mb-4">
                <div className="p-4">
                  <h3 className="text-xl mb-2 font-semibold text-gray-900">Amount: {expense.amount}</h3>
                    <>
                      <p className="text-sm text-gray-600">Description: {expense.description}</p>
                      <p className="text-sm text-gray-600">Uploaded: {formatDate(expense?.createdAt, {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric'
                      })}</p>
                      <a href={expense?.expenseMedia?.url} className="w-full h-full text-blue-800 object-cover">{expense?.expenseMedia?.originalName}</a>
                    </>
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
