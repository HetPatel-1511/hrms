import { useMutation, useQueryClient } from '@tanstack/react-query'
import { QUERY_KEY } from '../key';
import { addTravelPlanDocument } from '../api/travelPlan';

const useAddTravelPlanDocumentMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addTravelPlanDocument,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.EMPLOYEE_TRAVEL_PLAN_DOCUMENTS] });
        },
    })
}

export default useAddTravelPlanDocumentMutation
