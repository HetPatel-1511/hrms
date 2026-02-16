import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchEmployeeTravelPlanExpenses } from '../api/travelPlan'

const useEmployeeTravelPlanExpensesQuery = (travelPlanId: any, employeeId: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.EMPLOYEE_TRAVEL_PLAN_EXPENSES, travelPlanId, employeeId],
        queryFn: () => fetchEmployeeTravelPlanExpenses(travelPlanId, employeeId)
    })
}

export default useEmployeeTravelPlanExpensesQuery
