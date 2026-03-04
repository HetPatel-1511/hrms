import { useMutation, useQueryClient } from '@tanstack/react-query'
import { changeEmployeeRoles } from '../api/employees';
import { QUERY_KEY } from '../key';

const useChangeEmployeeRolesMutation = () => {
    const client = useQueryClient();
    return useMutation({
        mutationFn: changeEmployeeRoles,
        onSuccess: () => {
            client.invalidateQueries({ queryKey: [QUERY_KEY.EMPLOYEES] });
        },
    })
}

export default useChangeEmployeeRolesMutation
