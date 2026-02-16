import { useMutation, useQueryClient } from '@tanstack/react-query'
import { QUERY_KEY } from '../key';
import { addTravelPlanExpense } from '../api/travelPlan';

const useAddTravelPlanExpenseMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: addTravelPlanExpense,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.EMPLOYEE_TRAVEL_PLAN_EXPENSES] });
        },
    })
}

export default useAddTravelPlanExpenseMutation
