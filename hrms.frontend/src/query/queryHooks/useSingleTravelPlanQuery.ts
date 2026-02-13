import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchSingleTravelPlans } from '../api/travelPlan'

const useSingleTravelPlanQuery = (id: any) => {
    return useQuery({
        queryKey: [QUERY_KEY.TRAVEL_PLAN],
        queryFn: () => fetchSingleTravelPlans(id)
    })
}

export default useSingleTravelPlanQuery
