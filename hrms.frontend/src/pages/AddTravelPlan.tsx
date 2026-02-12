import React, { useEffect, useState } from 'react'
import Button from '../components/Button'
import { useForm } from 'react-hook-form';
import useAddTravelPlanMutation from '../query/queryHooks/useAddTravelPlanMutation';
import TextInput from '../components/TextInput';
import FormInput from '../components/TextInput';
import useEmployeesQuery from '../query/queryHooks/useEmployeeQuery';
import Select from 'react-select';
import { useNavigate } from 'react-router';

const AddTravelPlan = () => {
    const {
        register,
        handleSubmit,
        formState: { errors },
        setValue
    } = useForm();
    const [selectedEmployees, setSelectedEmployees] = useState<any>([]);


    const navigate = useNavigate()
    const addTravelPlan = useAddTravelPlanMutation()
    const { data, isLoading, error, isSuccess } = useEmployeesQuery()

    const onSubmit = (data: any) => {
        data.employees = selectedEmployees.map((e: any) => {return {id: e.value, maxAmountPerDay: e.maxAmountPerDay}})
        addTravelPlan.mutate(data);
    };

    useEffect(() => {
        if (addTravelPlan.isSuccess) {
            navigate("/travel-plan")
        }
    }, [addTravelPlan.isSuccess])

    const handleAmountChange = (employeeValue: any, amount: any) => {
        setSelectedEmployees((se: any) =>
            se.map((employee: any) =>
                employee.id == employeeValue
                    ? { ...employee, maxAmountPerDay: Number(amount) || 0 }
                    : employee
            )
        );
    };

    const onChange = (value: any) => {
        setValue("employees", value);
        
        console.log([...value.map(
            (v: any) => {
                return { ...v, id: v.value, maxAmountPerDay: 0 }
            }
        )]);
        
        setSelectedEmployees((se:any)=>[...value.map(
            (v: any) => {
                return { ...v, id: v.value, maxAmountPerDay: se.find((s:any)=>s.value==v.value)?.maxAmountPerDay || 0 }
            }
        )])
    }

    return (
        <div>
            <form className="">
                <div className="mb-5">
                    <FormInput
                        label="Place"
                        id="place"
                        placeholder="USA"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Place is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        label="Purpose"
                        id="purpose"
                        placeholder="Meet John Doe"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Purpose is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        type="date"
                        label="Start Date"
                        id="startDate"
                        placeholder=""
                        register={register}
                        errors={errors}
                        validation={{ required: 'Start Date is mandatory' }}
                    />
                </div>
                <div className="mb-5">
                    <FormInput
                        type="date"
                        label="End Date"
                        id="endDate"
                        placeholder=""
                        register={register}
                        errors={errors}
                        validation={{ required: 'End Date is mandatory' }}
                    />
                </div>
                {isSuccess ?
                    <FormInput
                        type="multi-select"
                        label="Travelling Employees"
                        options={data.data.map((e: any) => { return { value: e.id, label: e.email } })}
                        id="employees"
                        register={register}
                        errors={errors}
                        validation={{ required: 'Atleast one employees is mandatory' }}
                        onChange={onChange}
                    />
                    : <></>}
                <div>
                    <EmployeeAmountForm selectedEmployees={selectedEmployees} register={register} errors={errors} handleAmountChange={handleAmountChange} />
                </div>
                <Button onClick={handleSubmit(onSubmit)}>
                    Submit
                </Button>
            </form>
        </div>
    )
}

const EmployeeAmountForm = ({
  selectedEmployees,
  register,
  errors,
  handleAmountChange,
}:any) => {
  return (
    <>
      {selectedEmployees.map((employee: any) => (
        <div key={employee.id} className="flex items-center mb-4">
          <label className="block text-sm font-medium text-gray-700 min-w-0.5 mr-4">
            {employee.label}:
          </label>
          <input
            id={employee.value}
            type="number"
            defaultValue={employee.maxAmountPerDay}
            {...register(employee.value, {
              required: "Max amount is required",
              min: { value: 0, message: "Amount should be 0 or more" },
            })}
            onChange={(e) => handleAmountChange(employee.id, e.target.value)}
            className="border border-gray-300 rounded px-2 py-1 "
          />
          {errors[employee.value] && (
            <span style={{ color: 'red' }} className="text-sm">
              {errors[employee.value]?.message?.toString()}
            </span>
          )}
        </div>
      ))}
    </>
  );
};

export default AddTravelPlan
