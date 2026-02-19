import { useMutation, useQueryClient } from '@tanstack/react-query'
import { QUERY_KEY } from '../key';
import { addTravelPlanExpense, changeTravelPlanExpenseStatus } from '../api/travelPlan';

const useChangeTravelPlanExpenseStatusMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: changeTravelPlanExpenseStatus,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.EMPLOYEE_TRAVEL_PLAN_EXPENSES] });
        },
    })
}

export default useChangeTravelPlanExpenseStatusMutation
