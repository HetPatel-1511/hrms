import { useQuery } from '@tanstack/react-query'
import React from 'react'
import { QUERY_KEY } from '../key'
import { fetchTravelPlanById } from '../api/travelPlan'

const useSingleTravelPlanExpenseQuery = (expenseId?: string) => {
    return useQuery({
        queryKey: [QUERY_KEY.EMPLOYEE_TRAVEL_PLAN_EXPENSES, expenseId],
        queryFn: () => fetchTravelPlanById(expenseId),
        staleTime: 5000
    })
}

export default useSingleTravelPlanExpenseQuery
