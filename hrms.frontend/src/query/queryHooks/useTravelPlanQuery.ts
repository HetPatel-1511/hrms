import { useQuery } from '@tanstack/react-query'
import { QUERY_KEY } from '../key'
import { fetchTravelPlans } from '../api/travelPlan'

const useTravelPlanQuery = () => {
    return useQuery({
        queryKey: [QUERY_KEY.TRAVEL_PLANS],
        queryFn: fetchTravelPlans
    })
}

export default useTravelPlanQuery
