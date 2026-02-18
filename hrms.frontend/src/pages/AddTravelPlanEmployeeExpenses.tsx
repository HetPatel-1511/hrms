import React, { useEffect } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useAddTravelPlanExpenseMutation from '../query/queryHooks/useAddTravelPlanExpenseMutation';
import { useNavigate, useParams } from 'react-router';

const AddTravelPlanEmployeeExpenses = () => {
    const navigate = useNavigate();
    const { travelPlanId, employeeId } = useParams();
    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm();

    const addTravelPlanExpense = useAddTravelPlanExpenseMutation()
    
    const onSubmit = (data: any) => {
        const formData = new FormData();

        formData.append('amount', data.amount);
        formData.append('description', data.description);
        
        if (data.expenseMedias && data.expenseMedias.length > 0) {
            for (let i = 0; i < data.expenseMedias.length; i++) {
                formData.append('expenseMedias', data.expenseMedias[i]);
            }
        }
        
        addTravelPlanExpense.mutate({travelPlanId, employeeId, data: formData});
    };

    useEffect(() => {
        if (addTravelPlanExpense.isSuccess) {
            navigate(`/travel-plan/${travelPlanId}/employee/${employeeId}/expenses`)
        }
    }, [addTravelPlanExpense.isSuccess])

    return (
        <div>
            <h1 className='text-2xl mb-4'>Add Expense</h1>
            <form className="">
                <div className="mb-5">
                    <FormInput
                        type="number"
                        label="Amount"
                        id="amount"
                        placeholder="19.99"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Amount is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Description"
                        id="description"
                        placeholder=""
                        register={register}
                        errors={errors}
                        validation={{ required: 'Description is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        type="file"
                        label="Expense Proof"
                        id="expenseMedias"
                        multiple={true}
                        placeholder=""
                        register={register}
                        errors={errors}
                        validation={{ required: 'Expense Proof is mandatory' }}
                    />
                </div>
                <Button onClick={handleSubmit(onSubmit)}>
                    Submit
                </Button>
            </form>
        </div>
    )
}

export default AddTravelPlanEmployeeExpenses
