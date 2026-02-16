import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchEmployeeTravelPlanDocuments } from '../api/travelPlan'

const useEmployeeTravelPlanDocumentsQuery = (travelPlanId: any, employeeId: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.EMPLOYEE_TRAVEL_PLAN_DOCUMENTS, travelPlanId, employeeId],
        queryFn: () => fetchEmployeeTravelPlanDocuments(travelPlanId, employeeId)
    })
}

export default useEmployeeTravelPlanDocumentsQuery
