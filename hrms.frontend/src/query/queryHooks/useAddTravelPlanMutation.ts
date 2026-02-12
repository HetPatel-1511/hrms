import { useMutation} from '@tanstack/react-query'
import { addTravelPlan } from '../api/travelPlan';

const useAddTravelPlanMutation = () => {
    return useMutation({
        mutationFn: addTravelPlan
    })
}

export default useAddTravelPlanMutation
