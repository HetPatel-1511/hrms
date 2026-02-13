import { useMutation, useQueryClient } from '@tanstack/react-query'
import { addTravelPlan } from '../api/travelPlan';
import { QUERY_KEY } from '../key';

const useAddTravelPlanMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addTravelPlan,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.TRAVEL_PLANS] });
        },
    })
}

export default useAddTravelPlanMutation
