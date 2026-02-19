import React, { useEffect, useState } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import FormInput from '../components/TextInput';
import useAddTravelPlanExpenseMutation from '../query/queryHooks/useAddTravelPlanExpenseMutation';
import { useNavigate, useParams } from 'react-router';
import useSingleTravelPlanExpenseQuery from '../query/queryHooks/useSingleTravelPlanExpenseQuery';
import { XMarkIcon } from '@heroicons/react/24/solid';

const AddTravelPlanEmployeeExpenses = ({isDraftScreen=false}) => {
    const navigate = useNavigate();
    const { travelPlanId, employeeId, expenseId }: any = useParams();
    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm();

    const [medias, setMedias] = useState<any>([]);

    const addTravelPlanExpense = useAddTravelPlanExpenseMutation()
    let query: any;
    if (isDraftScreen) {
        query = useSingleTravelPlanExpenseQuery(expenseId)
    }
    const isSuccess = query?.isSuccess;

    const onSubmit = (data: any, status: any) => {
        const formData = new FormData();

        formData.append('amount', data.amount);
        formData.append('description', data.description);
        formData.append('status', status);
        if (isDraftScreen) {
            for (let i = 0; i < medias.length; i++) {
                formData.append('existingMediaIdsToKeep', medias[i].id);
            }
            formData.append('expenseId', expenseId);
        }
        
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

    useEffect(() => {
        if (isSuccess) {
            const expenseData = query?.data?.data;
            setValue("amount", expenseData.amount);
            setValue("description", expenseData.description);
            setMedias(query?.data?.data?.expenseMedias)
        }
    }, [query?.data?.data])

    const handleRemoveMedia = (mediaId: any) => {
        setMedias((medias: any)=>medias.filter((m:any)=> m.id!=mediaId))
    }

    return (
        <div>
            <h1 className='text-2xl mb-4'>Add Expense</h1>
            <form className="" onSubmit={e=>e.preventDefault()}>
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
                        validation={{}}
                    />
                    <div className='mt-2'>
                        {isDraftScreen && medias.map((media: any, index: number) => (
                          <div key={index} className="flex items-center">
                            <button onClick={(e) => {e.preventDefault(); handleRemoveMedia(media.id)}} className='w-4 h-4 mr-1 bg-gray-200 rounded-full cursor-pointer p-0.5'><XMarkIcon /></button>
                            <a href={media?.url} className="h-full text-blue-800">{media?.originalName}</a>
                          </div>
                        ))}
                      </div>
                </div>
                <Button className="m-1" onClick={handleSubmit((data)=>onSubmit(data, "Draft"))}>
                    Save Draft
                </Button>
                <Button className="m-1" onClick={handleSubmit((data)=>onSubmit(data, "Submitted"))}>
                    Submit
                </Button>
            </form>
        </div>
    )
}

export default AddTravelPlanEmployeeExpenses
