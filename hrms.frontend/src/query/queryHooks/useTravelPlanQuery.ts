import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchTravelPlans } from '../api/travelPlan'

const useTravelPlanQuery = (fetchAll: boolean) => {
    return useQuery({
        queryKey: [QUERY_KEY.TRAVEL_PLANS, fetchAll],
        queryFn: () => fetchTravelPlans(fetchAll)
    })
}

export default useTravelPlanQuery
